package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.utility.FileUtility;

public class PropertiesConfigurationSourceTest extends TestCase {

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
        assertEquals("bbb", configuration.closeBrowsersAfterTestRuns());
        assertEquals("ccc", configuration.logsDirectory());
        assertEquals("ddd", configuration.logStatus());
        assertEquals("eee", configuration.port());
        assertEquals("fff", configuration.remoteMachineURLs());
        assertEquals("ggg", configuration.resourceBase());
        assertEquals("hhh", configuration.timeoutSeconds());
        assertEquals("iii", configuration.url());
    }

    public void tearDown() throws Exception {
        FileUtility.deleteFile("temp.file");
        super.tearDown();
    }

    static void writePropertiesFile(String fileName) {
        String contents =
            ConfigurationProperty.BROWSER_FILE_NAMES.getName() + "=aaa\n" +
            ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName() + "=bbb\n" +
            ConfigurationProperty.LOGS_DIRECTORY.getName() + "=ccc\n" +
            ConfigurationProperty.LOG_STATUS.getName() + "=ddd\n" +
            ConfigurationProperty.PORT.getName() + "=eee\n" +
            ConfigurationProperty.REMOTE_MACHINE_URLS.getName() + "=fff\n"+
            ConfigurationProperty.RESOURCE_BASE.getName() + "=ggg\n" +
            ConfigurationProperty.TIMEOUT_SECONDS.getName() + "=hhh\n" +
            ConfigurationProperty.URL.getName() + "=iii\n";
        FileUtility.writeFile(contents, fileName);
    }

}
