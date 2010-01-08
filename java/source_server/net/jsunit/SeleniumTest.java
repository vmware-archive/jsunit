package net.jsunit;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;
import junit.framework.TestCase;
import net.jsunit.configuration.CompositeConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.configuration.ServerConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumTest extends TestCase {

    protected JsUnitServer server;

    protected static Selenium selenium;
    protected static String SELENIUM_SERVER_HOST = "selenium.server.host";
    protected static String SELENIUM_SERVER_PORT = "selenium.server.port";
    protected static String SELENIUM_BROWSER_STARTCOMMAND = "selenium.browser.startCommand";
    protected static String SELENIUM_BROWSER_URL = "selenium.browser.url";
    protected Process tunnel_process;
    protected String tunnel_id;
    protected HashMap<String, String> seleniumConfig;

    public void setUp() throws Exception {
        super.setUp();
        seleniumConfig = loadSeleniumConfig("../selenium.xml");
        startJsUnitServer();
        if (seleniumConfig.get("host").equals("saucelabs.com")) {
            startSauceTunnel();
        }
        startSeleniumClient();
    }

    public void tearDown() throws Exception {
        stopSeleniumClient();
        if (seleniumConfig.get("host").equals("saucelabs.com")) {
            stopSauceTunnel();
        }
        stopJsUnitServer();
        super.tearDown();
    }

    private void startSauceTunnel() throws IOException {
        System.out.println("Starting sauce tunnel...");
        tunnel_process = Runtime.getRuntime().exec("ruby sauce-tunnel-setup.rb");
        String line;
        BufferedReader input = new BufferedReader
                        (new InputStreamReader(tunnel_process.getInputStream()));
        Pattern pattern = Pattern.compile("\\[saucelabs-adapter\\] Tunnel ID (.*) for (.*) is up\\.$");

        while ((line = input.readLine()) != null) {
            System.out.println(line);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                tunnel_id = matcher.group(1);
                seleniumConfig.put("browser_url", "http://" + matcher.group(2));
                break;
            }
        }
        System.out.println("done starting tunnel");
    }

    private void stopSauceTunnel() throws IOException {
        System.out.println("stopping tunnel");
        tunnel_process = Runtime.getRuntime().exec("ruby sauce-tunnel-delete.rb " + tunnel_id);
    }

    public void testStandaloneRun() throws Exception {
        selenium.open("/jsunit/testRunner.html?testPage=/jsunit/tests/jsUnitTestSuite.html&autorun=true");
        waitForStatus();

        // window.frames['mainFrame'].frames['mainCounts'].frames['mainCountsRuns'].document.getElementById('content').innerHTML
        String runs = selenium.getEval("window.mainFrame.mainCounts.mainCountsRuns.document.getElementById('content').innerHTML").trim();
        String fails = selenium.getEval("window.mainFrame.mainCounts.mainCountsFailures.document.getElementById('content').innerHTML").trim();
        String errors = selenium.getEval("window.mainFrame.mainCounts.mainCountsErrors.document.getElementById('content').innerHTML").trim();

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

    private HashMap<String, String> loadSeleniumConfig(String configPath) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File(configPath));
        doc.getDocumentElement().normalize();
        Element seleniumConfigElement = (Element) doc.getElementsByTagName("selenium_config").item(0);
        HashMap<String, String> seleniumProperties = new HashMap<String, String>();

        String host = System.getProperty(SELENIUM_SERVER_HOST);
        String port = System.getProperty(SELENIUM_SERVER_PORT);
        String start_command = System.getProperty(SELENIUM_BROWSER_STARTCOMMAND);
        String browser_url = System.getProperty(SELENIUM_BROWSER_URL);

        if (host == null) {
            host = seleniumConfigElement.getElementsByTagName("host").item(0).getFirstChild().getNodeValue();
        }
        if (port == null) {
            port = seleniumConfigElement.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
        }
        if (start_command == null) {
            start_command = seleniumConfigElement.getElementsByTagName("browser_start_command").item(0).getFirstChild().getNodeValue();
        }
        // If we are using saucelabs the browser_user will be automatically generated.
        
        seleniumProperties.put("host", host);
        seleniumProperties.put("port", port);
        seleniumProperties.put("browser_start_command", start_command);
        seleniumProperties.put("browser_url", browser_url);
        return seleniumProperties;
    }

    private void startJsUnitServer() throws Exception {
        server = new JsUnitServer(new ServerConfiguration(new DelegatingConfigurationSource(CompositeConfigurationSource.resolve())));
        server.start();
    }

    private void stopJsUnitServer() {
        if (server != null) {
            server.dispose();
        }
    }

    private void startSeleniumClient() throws Exception {
        String host = seleniumConfig.get("host");
        int port = Integer.parseInt(seleniumConfig.get("port"));
        String start_command = seleniumConfig.get("browser_start_command");
        String browser_url = seleniumConfig.get("browser_url");
        selenium = new DefaultSelenium(host, port, start_command, browser_url);
        selenium.start();
    }

    private void stopSeleniumClient() {
        selenium.stop();
    }

    private void waitForStatus() throws InterruptedException {
        new Wait() {
            public boolean until() {
                String statusContent = selenium.getEval("window.mainFrame.mainStatus.document.getElementById('content').innerHTML");
                return statusContent.indexOf("Done") != -1;
            }
        }.wait("This didn't quite work");
    }
}
