package net.jsunit;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class PropertiesConfiguration extends JsUnitConfiguration {

    public static final String PROPERTIES_FILE_NAME = "jsunit.properties";

    private Properties properties;

    public PropertiesConfiguration() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
        } catch (Exception e) {
            throw new RuntimeException("Could not load " + PROPERTIES_FILE_NAME);
        }
    }

    protected String resourceBase() {
        return properties.getProperty(RESOURCE_BASE);
    }

    protected String logsDirectory() {
        return properties.getProperty(LOGS_DIRECTORY);
    }

    protected String port() {
        return properties.getProperty(PORT);
    }

    protected String url() {
        return properties.getProperty(URL);
    }

    protected String browserFileNames() {
        return properties.getProperty(BROWSER_FILE_NAMES);
    }

}