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
        return getProperty(ConfigurationProperty.RESOURCE_BASE.getName());
    }

    public String logsDirectory() {
        return getProperty(ConfigurationProperty.LOGS_DIRECTORY.getName());
    }

    public String port() {
        return getProperty(ConfigurationProperty.PORT.getName());
    }

    public String remoteMachineURLs() {
        return getProperty(ConfigurationProperty.REMOTE_MACHINE_URLS.getName());
    }

    public String url() {
        return getProperty(ConfigurationProperty.URL.getName());
    }

    public String browserFileNames() {
        return getProperty(ConfigurationProperty.BROWSER_FILE_NAMES.getName());
    }

    public String closeBrowsersAfterTestRuns() {
        return getProperty(ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName());
    }

	public String logStatus() {
		return getProperty(ConfigurationProperty.LOG_STATUS.getName());
	}

	public String timeoutSeconds() {
		return getProperty(ConfigurationProperty.TIMEOUT_SECONDS.getName());
	}

}