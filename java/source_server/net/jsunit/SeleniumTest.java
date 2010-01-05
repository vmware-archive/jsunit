package net.jsunit;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.configuration.CompositeConfigurationSource;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.model.Browser;
import net.jsunit.model.TestRunResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumTest extends TestCase {

//    protected JsUnitServer server;
//    private TestRunManager testRunManager;
//    private ConfigurationSource configurationSource;
    private String overrideURL;

    protected static Selenium selenium;
    protected static String SELENIUM_SERVER_HOST="selenium.server.host";
    protected static String SELENIUM_SERVER_PORT="selenium.server.port";
    protected static String SELENIUM_BROWSER_STARTCOMMAND="selenium.browser.startCommand";
    protected static String SELENIUM_BROWSER_URL="selenium.browser.url";

//    public SeleniumTest(String name) {
//        super(name);
//        this.configurationSource = configurationSource();
//    }
//
//    public SeleniumTest(ConfigurationSource source) {
//        super(source.browserFileNames());
//        this.configurationSource = source;
//    }

//    public static Test suite() {
//        System.out.println("IN SUITE");
//        TestSuite suite = new TestSuite();
//        ConfigurationSource originalSource = CompositeConfigurationSource.resolve();
//        ServerConfiguration configuration = new ServerConfiguration(originalSource);
//        for (final Browser browser : configuration.getBrowsers())
//            suite.addTest(new SeleniumTest(new DelegatingConfigurationSource(originalSource) {
//                public String browserFileNames() {
//                    return browser.getFullFileName();
//                }
//            }));
//        return suite;
//    }
//
    public void setUp() throws Exception {
        super.setUp();
//        server = new JsUnitServer(new ServerConfiguration(configurationSource));
//        server.start();
//        testRunManager = createTestRunManager();

        selenium = new DefaultSelenium(System.getProperty(SELENIUM_SERVER_HOST),
        Integer.parseInt(System.getProperty(SELENIUM_SERVER_PORT)),
        System.getProperty(SELENIUM_BROWSER_STARTCOMMAND),
        System.getProperty(SELENIUM_BROWSER_URL));
        selenium.start();
    }

//    protected ConfigurationSource configurationSource() {
//        return CompositeConfigurationSource.resolve();
//    }
//
//    protected TestRunManager createTestRunManager() {
//        return new TestRunManager(server, overrideURL);
//    }

    public void tearDown() throws Exception {
//        if (server != null)
//            server.dispose();
        super.tearDown();

        selenium.stop();
    }

    public void testStandaloneRun() throws Exception {
        selenium.open("http://localhost:8080/jsunit/testRunner.html?testPage=/jsunit/tests/jsUnitTestSuite.html&autorun=true");
        waitForStatus();

        String runs = selenium.getEval("window.frames['mainFrame'].frames['mainCounts'].frames['mainCountsRuns'].document.getElementById('content').textContent").trim();
        String fails = selenium.getEval("window.frames['mainFrame'].frames['mainCounts'].frames['mainCountsFailures'].document.getElementById('content').textContent").trim();
        String errors = selenium.getEval("window.frames['mainFrame'].frames['mainCounts'].frames['mainCountsErrors'].document.getElementById('content').textContent").trim();

        Pattern pattern = Pattern.compile("\\d+$");
        Matcher matcher = pattern.matcher(fails);
        matcher.find();
        Integer failCount = new Integer(matcher.group());

        matcher = pattern.matcher(errors);
        matcher.find();
        Integer errorCount = new Integer(matcher.group());

        matcher = pattern.matcher(runs);
        matcher.find();
        Integer runCount = new Integer(matcher.group());

        if (failCount + errorCount > 0) {
            System.out.printf("Testing complete, Runs: %s, Fails: %s, Errors: %s\n", runCount, failCount, errorCount);
            String errorMessages = selenium.getEval("window.frames['mainFrame'].frames['mainErrors'].document.getElementsByName('problemsList')[0].innerHTML");
            System.out.printf("Error messages: %s\n", errorMessages);

        }
        assertEquals(0, failCount + errorCount);
    }

    public void waitForStatus() throws InterruptedException {
        new Wait() {
            public boolean until() {
                String statusContent = selenium.getEval("window.frames['mainFrame'].frames['mainStatus'].document.getElementById('content').textContent");
                return statusContent.indexOf("Done") != -1;
            }
        }.wait("This didn't quite work");
    }
}
