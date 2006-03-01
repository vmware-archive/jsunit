package net.jsunit.configuration;

import net.jsunit.utility.FileUtility;
import junit.framework.TestCase;

public class ConfigurationSourceResolutionTest extends TestCase {

    public void testResolveArgumentsConfiguration() {
        ConfigurationSource source = Configuration.resolveSource(new String[] {"foo"});
        assertTrue(source instanceof ArgumentsConfigurationSource);
    }

    public void testResolveEnvironmentVariablesConfiguration() {
        System.setProperty(ConfigurationProperty.URL.getName(), "http://localhost:8080/");
        ConfigurationSource source = Configuration.resolveSource(new String[] {});
        assertTrue(source instanceof EnvironmentVariablesConfigurationSource);
    }

    public void testResolvePropertiesConfiguration() {
    	writePropertiesFile(PropertiesFileConfigurationSource.PROPERTIES_FILE_NAME);
        ConfigurationSource source = Configuration.resolveSource(new String[] {});
        assertTrue(source instanceof PropertiesFileConfigurationSource);
    }

    private void writePropertiesFile(String fileName) {
        FileUtility.writeFile(ConfigurationProperty.BROWSER_FILE_NAMES.getName() + "=aaa", fileName);
    }

    protected void tearDown() throws Exception {
        System.getProperties().remove(ConfigurationProperty.URL.getName());
        FileUtility.deleteFile(PropertiesFileConfigurationSource.PROPERTIES_FILE_NAME);
        super.tearDown();
    }

}