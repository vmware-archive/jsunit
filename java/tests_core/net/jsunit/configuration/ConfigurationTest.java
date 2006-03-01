package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.StubConfigurationSource;
import net.jsunit.utility.SystemUtility;
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
        assertEquals(new File("logs" + File.separator + "directory"), configuration.getLogsDirectory());
        assertEquals(1234, configuration.getPort());
        assertEquals(new File("resource" + File.separator + "base"), configuration.getResourceBase());
        assertEquals(new URL("http://www.example.com:1234"), configuration.getTestURL());
        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
        assertEquals(76, configuration.getTimeoutSeconds());
        List<URL> expectedRemoteMachineURLs = new ArrayList<URL>();
        expectedRemoteMachineURLs.add(new URL("http://localhost:8081/jsunit"));
        expectedRemoteMachineURLs.add(new URL("http://127.0.0.1:8082/jsunit"));
        assertEquals(expectedRemoteMachineURLs, configuration.getRemoteMachineURLs());
        assertTrue(configuration.shouldIgnoreUnresponsiveRemoteMachines());

        assertTrue(configuration.isValidFor(ServerType.STANDARD));
        assertTrue(configuration.isValidFor(ServerType.FARM));
    }

    public void testMinimal() throws Exception {
        Configuration configuration = new Configuration(new MinimalValidForBothConfigurationSource());
        assertEquals(new File("."), configuration.getResourceBase());
        assertEquals(new File("." + File.separator + "logs"), configuration.getLogsDirectory());
        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
        assertEquals(60, configuration.getTimeoutSeconds());
        assertFalse(configuration.shouldIgnoreUnresponsiveRemoteMachines());

        assertTrue(configuration.isValidFor(ServerType.STANDARD));
        assertTrue(configuration.isValidFor(ServerType.FARM));
    }

    public void testInvalidForStandardValidForFarm() throws Exception {
        Configuration configuration = new Configuration(new InvalidForStandardValidForFarmConfigurationSource());
        assertFalse(configuration.isValidFor(ServerType.STANDARD));
        assertEquals(1, ServerType.STANDARD.getPropertiesInvalidFor(configuration).size());
        assertTrue(configuration.isValidFor(ServerType.FARM));
    }
    
    public void testValidForStandardInvalidForFarm() throws Exception {
        Configuration configuration = new Configuration(new ValidForStandardInvalidForFarmConfigurationSource());
        assertTrue(configuration.isValidFor(ServerType.STANDARD));
        assertFalse(configuration.isValidFor(ServerType.FARM));
        assertEquals(1, ServerType.FARM.getPropertiesInvalidFor(configuration).size());
    }

    public void testAsXml() throws Exception {
      FullValidForBothConfigurationSource source
          = new FullValidForBothConfigurationSource();
      Configuration configuration = new Configuration(source);
      File logsDirectory = new File(source.logsDirectory());
      File resourceBase = new File(source.resourceBase());
      assertEquals(
          "<configuration type=\""+ServerType.STANDARD.name()+"\">" +
              "<os>"+SystemUtility.osString()+"</os>" +
              "<ipAddress>"+SystemUtility.ipAddress()+"</ipAddress>" +
              "<hostname>"+SystemUtility.hostname()+"</hostname>" +
              "<browserFileNames>" +
                  "<browserFileName>browser1.exe</browserFileName>" +
                  "<browserFileName>browser2.exe</browserFileName>" +
              "</browserFileNames>" +
              "<closeBrowsersAfterTestRuns>true</closeBrowsersAfterTestRuns>" +
              "<description>This is the best server ever</description>" +
              "<logsDirectory>" + logsDirectory.getAbsolutePath() + "</logsDirectory>" +
                  "<logStatus>true</logStatus>" +
                  "<port>1234</port>" +
                  "<resourceBase>" + resourceBase.getAbsolutePath() + "</resourceBase>" +
                  "<timeoutSeconds>76</timeoutSeconds>" +
                  "<url>http://www.example.com:1234</url>" +
          "</configuration>",
          XmlUtility.asString(configuration.asXml(ServerType.STANDARD))
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
        assertEquals(new File("logs" + File.separator + "directory").getAbsolutePath(), arguments[index++]);

        assertEquals("-logStatus", arguments[index++]);
        assertEquals("true", arguments[index++]);

        assertEquals("-port", arguments[index++]);
        assertEquals("1234", arguments[index++]);

        assertEquals("-remoteMachineURLs", arguments[index++]);
        assertEquals("http://localhost:8081/jsunit,http://127.0.0.1:8082/jsunit", arguments[index++]);

        assertEquals("-resourceBase", arguments[index++]);
        assertEquals(new File("resource/base").getAbsolutePath(), arguments[index++]);

        assertEquals("-timeoutSeconds", arguments[index++]);
        assertEquals("76", arguments[index++]);

        assertEquals("-url", arguments[index++]);
        assertEquals("http://www.example.com:1234", arguments[index]);
     }

    static class FullValidForBothConfigurationSource implements ConfigurationSource {

        public String resourceBase() {
            return "resource" + File.separator + "base";
        }

        public String port() {
            return "1234";
        }

        public String logsDirectory() {
            return "logs" + File.separator + "directory";
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
