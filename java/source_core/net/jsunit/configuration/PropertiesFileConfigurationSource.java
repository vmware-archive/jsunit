package net.jsunit.configuration;

import java.io.FileInputStream;
import java.util.Properties;

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

	private String propertValue(ConfigurationProperty property) {
		return properties.getProperty(property.getName());
	}

    public String resourceBase() {
        return propertValue(ConfigurationProperty.RESOURCE_BASE);
    }

    public String logsDirectory() {
        return propertValue(ConfigurationProperty.LOGS_DIRECTORY);
    }

    public String port() {
        return propertValue(ConfigurationProperty.PORT);
    }

    public String remoteMachineURLs() {
        return propertValue(ConfigurationProperty.REMOTE_MACHINE_URLS);
    }

    public String url() {
        return propertValue(ConfigurationProperty.URL);
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return propertValue(ConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES);
    }

    public String browserFileNames() {
        return propertValue(ConfigurationProperty.BROWSER_FILE_NAMES);
    }

    public String closeBrowsersAfterTestRuns() {
        return propertValue(ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS);
    }

	public String logStatus() {
		return propertValue(ConfigurationProperty.LOG_STATUS);
	}

	public String timeoutSeconds() {
		return propertValue(ConfigurationProperty.TIMEOUT_SECONDS);
	}

}