package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.Utility;

public class PropertiesConfigurationTest extends TestCase {

    public void testNoFile() throws Exception {
        try {
        	new PropertiesFileConfigurationSource("nosuch.file");
            fail("Should have thrown a RuntimeException because no properties file exists");
        } catch (RuntimeException e) {
        }
    }

    public void testSimple() throws Exception {
        writePropertiesFile("temp.file");
        PropertiesFileConfigurationSource configuration = new PropertiesFileConfigurationSource("temp.file");
        assertEquals("aaa", configuration.browserFileNames());
        assertEquals("bbb", configuration.logsDirectory());
        assertEquals("1234", configuration.port());
        assertEquals("ccc", configuration.resourceBase());
        assertEquals("ddd", configuration.url());
        assertEquals("false", configuration.closeBrowsersAfterTestRuns());
    }

    public void tearDown() throws Exception {
        Utility.deleteFile("temp.file");
        super.tearDown();
    }

    static void writePropertiesFile(String fileName) {
        String contents =
                ConfigurationSource.BROWSER_FILE_NAMES + "=aaa\n" +
                ConfigurationSource.LOGS_DIRECTORY + "=bbb\n" +
                ConfigurationSource.PORT + "=1234\n" +
                ConfigurationSource.RESOURCE_BASE + "=ccc\n" +
                ConfigurationSource.URL + "=ddd\n" +
                ConfigurationSource.CLOSE_BROWSERS_AFTER_TEST_RUNS + "=false\n";
        Utility.writeFile(contents, fileName);
    }

}