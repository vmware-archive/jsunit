package net.jsunit.configuration;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.net.URL;

import net.jsunit.Utility;

public class ConfigurationTest extends TestCase {

    public void testFull() throws Exception {
        Configuration configuration = new Configuration(new FullConfigurationSource());

        List<String> expectedBrowsers = new ArrayList<String>();
        expectedBrowsers.add("browser1.exe");
        expectedBrowsers.add("browser2.exe");
        assertEquals(expectedBrowsers, configuration.getBrowserFileNames());

        assertEquals(new File("c:\\logs\\directory"), configuration.getLogsDirectory());

        assertEquals(1234, configuration.getPort());

        assertEquals(new File("c:\\resource\\base"), configuration.getResourceBase());

        assertEquals(new URL("http://www.example.com"), configuration.getTestURL());

        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
    }

    public void testMinimal() throws Exception {
        Configuration configuration = new Configuration(new MinimalConfigurationSource());

        assertEquals(new File("."), configuration.getResourceBase());

        assertEquals(new File(".\\logs"), configuration.getLogsDirectory());

        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
    }

    public void testAsXml() throws Exception {
        Configuration configuration = new Configuration(new FullConfigurationSource());
        assertEquals(
                "<configuration>" +
                  "<resourceBase>c:\\resource\\base</resourceBase>" +
                  "<port>1234</port>" +
                  "<logsDirectory>c:\\logs\\directory</logsDirectory>" +
                  "<browserFileNames>" +
                    "<browserFileName>browser1.exe</browserFileName>" +
                    "<browserFileName>browser2.exe</browserFileName>" +
                  "</browserFileNames>" +
                  "<url>http://www.example.com</url>" +
                  "<closeBrowsersAfterTestRuns>true</closeBrowsersAfterTestRuns>" +
                "</configuration>",
                Utility.asString(configuration.asXml())
        );
    }

    static class FullConfigurationSource implements ConfigurationSource {

        public String resourceBase() {
            return "c:\\resource\\base";
        }

        public String port() {
            return "1234";
        }

        public String logsDirectory() {
            return "c:\\logs\\directory";
        }

        public String browserFileNames() {
            return "browser1.exe,browser2.exe";
        }

        public String url() {
            return "http://www.example.com";
        }

        public String closeBrowsersAfterTestRuns() {
            return "true";
        }
    }

    static class MinimalConfigurationSource implements ConfigurationSource {

        public String resourceBase() {
            return "";
        }

        public String port() {
            return "1234";
        }

        public String logsDirectory() {
            return "";
        }

        public String browserFileNames() {
            return "browser1.exe";
        }

        public String url() {
            return "http://www.example.com";
        }

        public String closeBrowsersAfterTestRuns() {
            return "";
        }
    }
}
