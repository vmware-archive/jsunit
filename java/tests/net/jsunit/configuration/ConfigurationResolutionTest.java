package net.jsunit.configuration;

import net.jsunit.Utility;
import junit.framework.TestCase;

public class ConfigurationResolutionTest extends TestCase {

    public void testResolveArgumentsConfiguration() {
        Configuration configuration = Configuration.resolve(new String[] {"foo"});
        assertTrue(configuration.getSource() instanceof ArgumentsConfigurationSource);
    }

    public void testResolveEnvironmentVariablesConfiguration() {
        System.setProperty(ConfigurationSource.URL, "foo");
        Configuration configuration = Configuration.resolve(new String[] {});
        assertTrue(configuration.getSource() instanceof EnvironmentVariablesConfigurationSource);
    }

    public void testResolvePropertiesConfiguration() {
    	PropertiesConfigurationTest.writePropertiesFile(PropertiesFileConfigurationSource.PROPERTIES_FILE_NAME);
        Configuration configuration = Configuration.resolve(new String[] {});
        assertTrue(configuration.getSource() instanceof PropertiesFileConfigurationSource);
    }

    protected void tearDown() throws Exception {
        System.getProperties().clear();
        Utility.deleteFile(PropertiesFileConfigurationSource.PROPERTIES_FILE_NAME);
        super.tearDown();
    }

}