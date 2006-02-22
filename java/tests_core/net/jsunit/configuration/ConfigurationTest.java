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
        
        assertTrue(configuration.isValidFor(ConfigurationType.STANDARD));
        assertTrue(configuration.isValidFor(ConfigurationType.FARM));
    }

    public void testMinimal() throws Exception {
        Configuration configuration = new Configuration(new MinimalValidForBothConfigurationSource());
        assertEquals(new File("."), configuration.getResourceBase());
        assertEquals(new File("." + File.separator + "logs"), configuration.getLogsDirectory());
        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
        assertEquals(60, configuration.getTimeoutSeconds());

        assertTrue(configuration.isValidFor(ConfigurationType.STANDARD));
        assertTrue(configuration.isValidFor(ConfigurationType.FARM));
    }

    public void testInvalidForStandardValidForFarm() throws Exception {
        Configuration configuration = new Configuration(new InvalidForStandardValidForFarmConfigurationSource());
        assertFalse(configuration.isValidFor(ConfigurationType.STANDARD));
        assertEquals(1, configuration.getPropertiesInvalidFor(ConfigurationType.STANDARD).size());
        assertTrue(configuration.isValidFor(ConfigurationType.FARM));
    }
    
    public void testValidForStandardInvalidForFarm() throws Exception {
        Configuration configuration = new Configuration(new ValidForStandardInvalidForFarmConfigurationSource());
        assertTrue(configuration.isValidFor(ConfigurationType.STANDARD));
        assertFalse(configuration.isValidFor(ConfigurationType.FARM));
        assertEquals(1, configuration.getPropertiesInvalidFor(ConfigurationType.FARM).size());
    }

    public void testAsXml() throws Exception {
        Configuration configuration = new Configuration(new FullValidForBothConfigurationSource());
        assertEquals(
            "<configuration>" +
                "<os>"+OperatingSystemUtility.osString()+"</os>" +
            	"<browserFileNames>" +
	            	"<browserFileName>browser1.exe</browserFileName>" +
	            	"<browserFileName>browser2.exe</browserFileName>" +
	            "</browserFileNames>" +
	            "<closeBrowsersAfterTestRuns>true</closeBrowsersAfterTestRuns>" +
	            "<logsDirectory>c:\\logs\\directory</logsDirectory>" +
	            "<logStatus>true</logStatus>" +
	            "<port>1234</port>" +
	            "<remoteMachineURLs>" +
	            	"<remoteMachineURL>http://localhost:8081</remoteMachineURL>" +
	            	"<remoteMachineURL>http://127.0.0.1:8082</remoteMachineURL>" +
	            "</remoteMachineURLs>" +
	            "<resourceBase>c:\\resource\\base</resourceBase>" +
	            "<timeoutSeconds>76</timeoutSeconds>" +
	            "<url>http://www.example.com:1234</url>" +
            "</configuration>",
            XmlUtility.asString(configuration.asXml())
        );
    }
    
    public void testAsArgumentsArray() throws Exception {
        Configuration configuration = new Configuration(new FullValidForBothConfigurationSource());
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
        assertEquals("http://www.example.com:1234", arguments[17]);
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

    static class MinimalValidForBothConfigurationSource extends StubConfigurationSource {

        public String browserFileNames() {
            return "browser1.exe";
        }

        public String remoteMachineURLs() {
            return "http://localhost:8081,http://127.0.0.1:8082";
        }

    }

    static class InvalidForStandardValidForFarmConfigurationSource extends StubConfigurationSource {

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
