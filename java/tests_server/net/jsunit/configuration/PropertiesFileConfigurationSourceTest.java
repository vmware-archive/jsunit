package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.utility.FileUtility;

import java.io.File;
import java.io.FileNotFoundException;

public class PropertiesFileConfigurationSourceTest extends TestCase {

    public void testNoFile() throws Exception {
        try {
            new PropertiesFileConfigurationSource("nosuch.file");
            fail("Should have thrown a RuntimeException because no properties file exists");
        } catch (FileNotFoundException e) {
        }
    }

    public void testSimple() throws Exception {
        writePropertiesFile("temp.file");
        PropertiesFileConfigurationSource configuration = new PropertiesFileConfigurationSource("temp.file");
        assertEquals("aaa", configuration.browserFileNames());
        assertEquals("bbb", configuration.closeBrowsersAfterTestRuns());
        assertEquals("ccc", configuration.logsDirectory());
        assertEquals("eee", configuration.port());
        assertEquals("fff", configuration.remoteMachineURLs());
        assertEquals("ggg", configuration.resourceBase());
        assertEquals("hhh", configuration.timeoutSeconds());
        assertEquals("iii", configuration.url());
    }

    public void tearDown() throws Exception {
        FileUtility.delete(new File("temp.file"));
        super.tearDown();
    }

    private void writePropertiesFile(String fileName) {
        String contents =
                ServerConfigurationProperty.BROWSER_FILE_NAMES.getName() + "=aaa\n" +
                        ServerConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName() + "=bbb\n" +
                        ServerConfigurationProperty.LOGS_DIRECTORY.getName() + "=ccc\n" +
                        ServerConfigurationProperty.PORT.getName() + "=eee\n" +
                        ServerConfigurationProperty.REMOTE_MACHINE_URLS.getName() + "=fff\n" +
                        ServerConfigurationProperty.RESOURCE_BASE.getName() + "=ggg\n" +
                        ServerConfigurationProperty.TIMEOUT_SECONDS.getName() + "=hhh\n" +
                        ServerConfigurationProperty.URL.getName() + "=iii\n";
        FileUtility.write(new File(fileName), contents);
    }

}
