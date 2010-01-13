package net.jsunit;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;
import junit.framework.TestCase;
import net.jsunit.configuration.CompositeConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.configuration.ServerConfiguration;
import org.ho.yaml.Yaml;
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
    protected Process tunnel_process;
    protected String tunnel_id;
    protected HashMap<String, String> seleniumConfig;

    public void setUp() throws Exception {
        super.setUp();
        seleniumConfig = loadSeleniumConfig(System.getProperty("seleniumEnvironment"));
        startJsUnitServer();
        if (seleniumConfig.get("selenium_server_address").equals("saucelabs.com")) {
            startSauceTunnel();
        }
        startSeleniumClient();
    }

    public void tearDown() throws Exception {
        stopSeleniumClient();
        if (seleniumConfig.get("selenium_server_address").equals("saucelabs.com")) {
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
                seleniumConfig.put("application_address", matcher.group(2));
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

    private HashMap<String, String>loadSeleniumConfig(String environment) throws Exception {
        String seleniumConfigFilePath = System.getProperty("seleniumConfigFilePath");
        HashMap<String, Object> environments = (HashMap<String, Object>)Yaml.load(new File(seleniumConfigFilePath));
        HashMap<String, String> config = (HashMap<String, String>)environments.get(environment);
        return config;
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
        String host = seleniumConfig.get("selenium_server_address");
        int port = Integer.parseInt(seleniumConfig.get("selenium_server_port"));
        String start_command = seleniumConfig.get("selenium_browser_key");
        String browser_url = "http://" + seleniumConfig.get("application_address") + ":" + seleniumConfig.get("application_port");
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
