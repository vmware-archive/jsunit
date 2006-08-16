package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.utility.FileUtility;

import java.io.File;

public class ConfigurationSourceResolutionTest extends TestCase {

    public void testResolveArgumentsConfiguration() {
        ConfigurationSource source = CompositeConfigurationSource.forArguments(new String[]{"-url", "foo"});
        assertEquals("foo", source.url());
    }

    public void testResolveEnvironmentVariablesConfiguration() {
        System.setProperty(ServerConfigurationProperty.URL.getName(), "http://localhost:8080/");
        ConfigurationSource source = CompositeConfigurationSource.forArguments(new String[]{});
        assertEquals("http://localhost:8080/", source.url());
    }

    public void testResolvePropertiesConfiguration() {
        writePropertiesFile(PropertiesFileConfigurationSource.PROPERTIES_FILE_NAME,
                ServerConfigurationProperty.BROWSER_FILE_NAMES.getName() + "=aaa");
        ConfigurationSource source = CompositeConfigurationSource.forArguments(new String[]{});
        assertEquals("aaa", source.browserFileNames());
    }

    private void writePropertiesFile(String fileName, String contents) {
        FileUtility.write(new File(fileName), contents);
    }

    protected void tearDown() throws Exception {
        System.getProperties().remove(ServerConfigurationProperty.URL.getName());
        FileUtility.delete(new File(PropertiesFileConfigurationSource.PROPERTIES_FILE_NAME));
        super.tearDown();
    }

}
