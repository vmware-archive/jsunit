package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.StubConfigurationSource;
import net.jsunit.utility.OperatingSystemUtility;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationTest extends TestCase {

    public void testFull() throws Exception {
        Configuration configuration = new Configuration(new FullValidForBothConfigurationSource());

        List<String> expectedBrowsers = new ArrayList<String>();
        expectedBrowsers.add("browser1.exe");
        expectedBrowsers.add("browser2.exe");
        assertEquals(expectedBrowsers, configuration.getBrowserFileNames());
        assertEquals(new File("c:\\logs\\directory"), configuration.getLogsDirectory());
        assertEquals(1234, configuration.getPort());
        assertEquals(new File("c:\\resource\\base"), configuration.getResourceBase());
        assertEquals(new URL("http://www.example.com:1234"), configuration.getTestURL());
        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
        assertEquals(76, configuration.getTimeoutSeconds());
        List<URL> expectedRemoteMachineURLs = new ArrayList<URL>();
        expectedRemoteMachineURLs.add(new URL("http://localhost:8081"));
        expectedRemoteMachineURLs.add(new URL("http://127.0.0.1:8082"));
        assertEquals(expectedRemoteMachineURLs, configuration.getRemoteMachineURLs());
        assertTrue(configuration.shouldIgnoreUnresponsiveRemoteMachines());

        assertTrue(configuration.isValidFor(ConfigurationType.STANDARD));
        assertTrue(configuration.isValidFor(ConfigurationType.FARM));
    }

    public void testMinimal() throws Exception {
        Configuration configuration = new Configuration(new MinimalValidForBothConfigurationSource());
        assertEquals(new File("."), configuration.getResourceBase());
        assertEquals(new File("." + File.separator + "logs"), configuration.getLogsDirectory());
        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
        assertEquals(60, configuration.getTimeoutSeconds());
        assertFalse(configuration.shouldIgnoreUnresponsiveRemoteMachines());

        assertTrue(configuration.isValidFor(ConfigurationType.STANDARD));
        assertTrue(configuration.isValidFor(ConfigurationType.FARM));
    }

    public void testInvalidForStandardValidForFarm() throws Exception {
        Configuration configuration = new Configuration(new InvalidForStandardValidForFarmConfigurationSource());
        assertFalse(configuration.isValidFor(ConfigurationType.STANDARD));
        assertEquals(1, ConfigurationType.STANDARD.getPropertiesInvalidFor(configuration).size());
        assertTrue(configuration.isValidFor(ConfigurationType.FARM));
    }
    
    public void testValidForStandardInvalidForFarm() throws Exception {
        Configuration configuration = new Configuration(new ValidForStandardInvalidForFarmConfigurationSource());
        assertTrue(configuration.isValidFor(ConfigurationType.STANDARD));
        assertFalse(configuration.isValidFor(ConfigurationType.FARM));
        assertEquals(1, ConfigurationType.FARM.getPropertiesInvalidFor(configuration).size());
    }

    public void testAsXml() throws Exception {
        Configuration configuration = new Configuration(new FullValidForBothConfigurationSource());
        assertEquals(
            "<configuration type=\""+ConfigurationType.STANDARD.name()+"\">" +
                "<os>"+OperatingSystemUtility.osString()+"</os>" +
                "<browserFileNames>" +
                    "<browserFileName>browser1.exe</browserFileName>" +
                    "<browserFileName>browser2.exe</browserFileName>" +
                "</browserFileNames>" +
                "<closeBrowsersAfterTestRuns>true</closeBrowsersAfterTestRuns>" +
                "<description>This is the best server ever</description>" +
                "<logsDirectory>c:\\logs\\directory</logsDirectory>" +
	            "<logStatus>true</logStatus>" +
	            "<port>1234</port>" +
	            "<resourceBase>c:\\resource\\base</resourceBase>" +
	            "<timeoutSeconds>76</timeoutSeconds>" +
	            "<url>http://www.example.com:1234</url>" +
            "</configuration>",
            XmlUtility.asString(configuration.asXml(ConfigurationType.STANDARD))
        );
    }
    
    public void testAsArgumentsArray() throws Exception {
        Configuration configuration = new Configuration(new FullValidForBothConfigurationSource());
        String[] arguments = configuration.asArgumentsArray();

        assertEquals(22, arguments.length);
        int index = 0;

        assertEquals("-browserFileNames", arguments[index++]);
        assertEquals("browser1.exe,browser2.exe", arguments[index++]);

        assertEquals("-closeBrowsersAfterTestRuns", arguments[index++]);
        assertEquals("true", arguments[index++]);

        assertEquals("-description", arguments[index++]);
        assertEquals("This is the best server ever", arguments[index++]);

        assertEquals("-ignoreUnresponsiveRemoteMachines", arguments[index++]);
        assertEquals("true", arguments[index++]);

        assertEquals("-logsDirectory", arguments[index++]);
        assertEquals("c:\\logs\\directory", arguments[index++]);

        assertEquals("-logStatus", arguments[index++]);
        assertEquals("true", arguments[index++]);

        assertEquals("-port", arguments[index++]);
        assertEquals("1234", arguments[index++]);

        assertEquals("-remoteMachineURLs", arguments[index++]);
        assertEquals("http://localhost:8081,http://127.0.0.1:8082", arguments[index++]);

        assertEquals("-resourceBase", arguments[index++]);
        assertEquals("c:\\resource\\base", arguments[index++]);

        assertEquals("-timeoutSeconds", arguments[index++]);
        assertEquals("76", arguments[index++]);

        assertEquals("-url", arguments[index++]);
        assertEquals("http://www.example.com:1234", arguments[index]);
     }

    static class FullValidForBothConfigurationSource implements ConfigurationSource {

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
            return "http://www.example.com:1234/";
        }

        public String ignoreUnresponsiveRemoteMachines() {
            return "true";
        }

        public String closeBrowsersAfterTestRuns() {
            return "true";
        }

        public String description() {
            return "This is the best server ever";
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

    static class MinimalValidForBothConfigurationSource extends StubConfigurationSource {

        public String browserFileNames() {
            return "browser1.exe";
        }

        public String remoteMachineURLs() {
            return "http://localhost:8081,http://127.0.0.1:8082";
        }

    }

    static class InvalidForStandardValidForFarmConfigurationSource extends StubConfigurationSource {

        public String timeoutSeconds() {
            return "xyz";
        }

        public String remoteMachineURLs() {
            return "http://localhost:8081,http://127.0.0.1:8082";
        }

    }

    static class ValidForStandardInvalidForFarmConfigurationSource extends StubConfigurationSource {

        public String browserFileNames() {
            return "mybrowser.exe";
        }

    }
}
