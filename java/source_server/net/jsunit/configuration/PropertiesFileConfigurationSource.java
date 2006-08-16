package net.jsunit.configuration;

import net.jsunit.utility.SystemUtility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class PropertiesFileConfigurationSource implements ConfigurationSource {

    public static final String PROPERTIES_FILE_NAME = "jsunit.properties";

    private Properties properties;
    private String propertiesFileName;

    public PropertiesFileConfigurationSource(String propertiesFileName) throws FileNotFoundException {
        this.propertiesFileName = propertiesFileName;
        loadProperties();
    }

    public PropertiesFileConfigurationSource() throws FileNotFoundException {
        this(PROPERTIES_FILE_NAME);
    }

    private void loadProperties() throws FileNotFoundException {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(propertiesFileName);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    protected String propertyValue(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public String resourceBase() {
        return propertyValue(ServerConfigurationProperty.RESOURCE_BASE.getName());
    }

    public String logsDirectory() {
        return propertyValue(ServerConfigurationProperty.LOGS_DIRECTORY.getName());
    }

    public String port() {
        return propertyValue(ServerConfigurationProperty.PORT.getName());
    }

    public String remoteMachineURLs() {
        return propertyValue(ServerConfigurationProperty.REMOTE_MACHINE_URLS.getName());
    }

    public String url() {
        return propertyValue(ServerConfigurationProperty.URL.getName());
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return propertyValue(ServerConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES.getName());
    }

    public String browserFileNames() {
        return propertyValue(ServerConfigurationProperty.BROWSER_FILE_NAMES.getName());
    }

    public String closeBrowsersAfterTestRuns() {
        return propertyValue(ServerConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName());
    }

    public String description() {
        return propertyValue(ServerConfigurationProperty.DESCRIPTION.getName());
    }

    public String timeoutSeconds() {
        return propertyValue(ServerConfigurationProperty.TIMEOUT_SECONDS.getName());
    }

    public String osString() {
        return SystemUtility.osString();
    }

    public String ipAddress() {
        return SystemUtility.ipAddress();
    }

    public String hostname() {
        return SystemUtility.hostname();
    }

}