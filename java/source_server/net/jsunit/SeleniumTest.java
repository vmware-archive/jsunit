package net.jsunit;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;
import junit.framework.TestCase;
import net.jsunit.configuration.CompositeConfigurationSource;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.configuration.ServerConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumTest extends TestCase {

    protected JsUnitServer server;

    protected static Selenium selenium;
    protected static String SELENIUM_SERVER_HOST = "selenium.server.host";
    protected static String SELENIUM_SERVER_PORT = "selenium.server.port";
    protected static String SELENIUM_BROWSER_STARTCOMMAND = "selenium.browser.startCommand";
    protected static String SELENIUM_BROWSER_URL = "selenium.browser.url";

    public void setUp() throws Exception {
        super.setUp();
        server = new JsUnitServer(new ServerConfiguration(new DelegatingConfigurationSource(CompositeConfigurationSource.resolve())));
        server.start();

        selenium = new DefaultSelenium(System.getProperty(SELENIUM_SERVER_HOST),
                Integer.parseInt(System.getProperty(SELENIUM_SERVER_PORT)),
                System.getProperty(SELENIUM_BROWSER_STARTCOMMAND),
                System.getProperty(SELENIUM_BROWSER_URL));
        selenium.start();
    }

    public void tearDown() throws Exception {
        if (server != null) {
            server.dispose();
        }

        selenium.stop();
        super.tearDown();
    }

    public void testStandaloneRun() throws Exception {
        selenium.open("http://localhost:8080/jsunit/testRunner.html?testPage=/jsunit/tests/jsUnitTestSuite.html&autorun=true");
        waitForStatus();

        // window.frames['mainFrame'].frames['mainCounts'].frames['mainCountsRuns'].document.getElementById('content').textContent
        String runs = selenium.getEval("window.mainFrame.mainCounts.mainCountsRuns.document.getElementById('content').textContent").trim();
        String fails = selenium.getEval("window.mainFrame.mainCounts.mainCountsFailures.document.getElementById('content').textContent").trim();
        String errors = selenium.getEval("window.mainFrame.mainCounts.mainCountsErrors.document.getElementById('content').textContent").trim();

        Pattern pattern = Pattern.compile("\\d+$");
        Matcher matcher = pattern.matcher(runs);
        matcher.find();
        Integer runCount = new Integer(matcher.group());

        matcher = pattern.matcher(fails);
        matcher.find();
        Integer failCount = new Integer(matcher.group());

        matcher = pattern.matcher(errors);
        matcher.find();
        Integer errorCount = new Integer(matcher.group());

        System.out.printf("********** JSUnit tests complete, Runs: %s, Fails: %s, Errors: %s **********\n",
                runCount, failCount, errorCount);

        if (failCount + errorCount > 0) {
            String errorMessages = selenium.getEval("window.mainFrame.mainErrors.document.getElementsByName('problemsList')[0].innerHTML");
            System.out.printf("Error messages: %s\n", errorMessages);

        }
        assertEquals(0, failCount + errorCount);
    }

    public void waitForStatus() throws InterruptedException {
        new Wait() {
            public boolean until() {
                String statusContent = selenium.getEval("window.mainFrame.mainStatus.document.getElementById('content').textContent");
                return statusContent.indexOf("Done") != -1;
            }
        }.wait("This didn't quite work");
    }
}
