package net.jsunit.configuration;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class PropertiesFileConfigurationSource implements ConfigurationSource {
 
    public static final String PROPERTIES_FILE_NAME = "jsunit.properties";

    private Properties properties;
    private String propertiesFileName;

    public PropertiesFileConfigurationSource(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
        loadProperties();
    }

    public PropertiesFileConfigurationSource() {
        this(PROPERTIES_FILE_NAME);
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(propertiesFileName);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not load " + propertiesFileName);
        }
    }

	private String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}

    public String resourceBase() {
        return getProperty(ConfigurationConstants.RESOURCE_BASE);
    }

    public String logsDirectory() {
        return getProperty(ConfigurationConstants.LOGS_DIRECTORY);
    }

    public String port() {
        return getProperty(ConfigurationConstants.PORT);
    }

    public String url() {
        return getProperty(ConfigurationConstants.URL);
    }

    public String browserFileNames() {
        return getProperty(ConfigurationConstants.BROWSER_FILE_NAMES);
    }

    public String closeBrowsersAfterTestRuns() {
        return getProperty(ConfigurationConstants.CLOSE_BROWSERS_AFTER_TEST_RUNS);
    }

	public String logStatus() {
		return getProperty(ConfigurationConstants.LOG_STATUS);
	}

}