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
        return getProperty(RESOURCE_BASE);
    }

    public String logsDirectory() {
        return getProperty(LOGS_DIRECTORY);
    }

    public String port() {
        return getProperty(PORT);
    }

    public String url() {
        return getProperty(URL);
    }

    public String browserFileNames() {
        return getProperty(BROWSER_FILE_NAMES);
    }

    public String closeBrowsersAfterTestRuns() {
        return getProperty(CLOSE_BROWSERS_AFTER_TEST_RUNS);
    }

}