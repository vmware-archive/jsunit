package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.Utility;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        assertEquals(76, configuration.getTimeoutSeconds());
        List<URL> expectedRemoteMachineURLs = new ArrayList<URL>();
        expectedRemoteMachineURLs.add(new URL("http://localhost:8081"));
        expectedRemoteMachineURLs.add(new URL("http://127.0.0.1:8082"));
        assertEquals(expectedRemoteMachineURLs, configuration.getRemoteMachineURLs());
    }

    public void testMinimal() throws Exception {
        Configuration configuration = new Configuration(new MinimalConfigurationSource());
        assertEquals(new File("."), configuration.getResourceBase());
        assertEquals(new File(".\\logs"), configuration.getLogsDirectory());
        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
        assertEquals(60, configuration.getTimeoutSeconds());
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
                "<timeoutSeconds>76</timeoutSeconds>" +
                "<remoteMachineURLs>" +
                    "<remoteMachineURL>http://localhost:8081</remoteMachineURL>" +
                    "<remoteMachineURL>http://127.0.0.1:8082</remoteMachineURL>" +
                "</remoteMachineURLs>" +
            "</configuration>",
            Utility.asString(configuration.asXml())
        );
    }
    
    public void testAsArgumentsArray() throws Exception {
        Configuration configuration = new Configuration(new FullConfigurationSource());
        String[] arguments = configuration.asArgumentsArray();

        assertEquals(18, arguments.length);

        assertEquals("-browserFileNames", arguments[0]);
        assertEquals("browser1.exe,browser2.exe", arguments[1]);

        assertEquals("-closeBrowsersAfterTestRuns", arguments[2]);
        assertEquals("true", arguments[3]);

        assertEquals("-logsDirectory", arguments[4]);
        assertEquals("c:\\logs\\directory", arguments[5]);

        assertEquals("-logStatus", arguments[6]);
        assertEquals("true", arguments[7]);

        assertEquals("-port", arguments[8]);
        assertEquals("1234", arguments[9]);

        assertEquals("-remoteMachineURLs", arguments[10]);
        assertEquals("http://localhost:8081,http://127.0.0.1:8082", arguments[11]);

        assertEquals("-resourceBase", arguments[12]);
        assertEquals("c:\\resource\\base", arguments[13]);

        assertEquals("-timeoutSeconds", arguments[14]);
        assertEquals("76", arguments[15]);

        assertEquals("-url", arguments[16]);
        assertEquals("http://www.example.com", arguments[17]);
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

		public String timeoutSeconds() {
			return "76";
		}

        public String remoteMachineURLs() {
            return "http://localhost:8081,http://127.0.0.1:8082";
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

		public String timeoutSeconds() {
			return "";
		}

        public String remoteMachineURLs() {
            return "http://localhost:8081,http://127.0.0.1:8082";
        }

    }
}
