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
                  "<logStatus>true</logStatus>" +                  
                "</configuration>",
                Utility.asString(configuration.asXml())
        );
    }
    
    public void testAsArgumentsArray() throws Exception {
        Configuration configuration = new Configuration(new FullConfigurationSource());
        String[] arguments = configuration.asArgumentsArray();
        assertEquals(14, arguments.length);
        assertEquals("-resourceBase", arguments[0]);
        assertEquals("c:\\resource\\base", arguments[1]);
        assertEquals("-port", arguments[2]);
        assertEquals("1234", arguments[3]);
        assertEquals("-logsDirectory", arguments[4]);
        assertEquals("c:\\logs\\directory", arguments[5]);
        assertEquals("-browserFileNames", arguments[6]);
        assertEquals("browser1.exe,browser2.exe", arguments[7]);
        assertEquals("-url", arguments[8]);
        assertEquals("http://www.example.com", arguments[9]);
        assertEquals("-closeBrowsersAfterTestRuns", arguments[10]);
        assertEquals("true", arguments[11]);
        assertEquals("-logStatus", arguments[12]);
        assertEquals("true", arguments[13]);
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

		public String logStatus() {
			return String.valueOf(true);
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

		public String logStatus() {
			return "";
		}
    }
}
