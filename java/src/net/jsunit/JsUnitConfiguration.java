package net.jsunit;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class JsUnitConfiguration {

    public static String PROPERTY_PORT = "port";
    public static String PROPERTY_RESOURCE_BASE = "resourceBase";
    public static String PROPERTY_LOGS_DIRECTORY = "logsDirectory";
    public static String PROPERTIES_FILE_NAME = "jsunit.properties";
    public static final String PROPERTY_REMOTE_MACHINE_NAMES = "remoteMachineNames";
    public static final String PROPERTY_URL = "url";
    public static final String PROPERTY_BROWSER_FILE_NAMES = "browserFileNames";

    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_RESOURCE_BASE = ".";

    public File resourceBase(Properties properties) {
        String result = properties.getProperty(PROPERTY_RESOURCE_BASE);
        if (Utility.isEmpty(result))
            result = DEFAULT_RESOURCE_BASE;
        return new File(result);
    }

    public File logsDirectory(Properties properties) {
        String result = properties.getProperty(PROPERTY_LOGS_DIRECTORY);
        if (Utility.isEmpty(result))
            result = resourceBase(properties) + File.separator + "java" + File.separator + "logs";
        return new File(result);
    }

    public int port(Properties properties) {
        int result;
        String portString = properties.getProperty(PROPERTY_PORT);
        if (Utility.isEmpty(portString))
            result = DEFAULT_PORT;
        else
            result = Integer.parseInt(portString);
        return result;
    }

    public void configureFromPropertiesFile(JsUnitServer server) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
        } catch (Exception e) {
            throw new RuntimeException("Could not load " + PROPERTIES_FILE_NAME);
        }
        server.setResourceBase(resourceBase(properties));
        server.setPort(port(properties));
        server.setLogsDirectory(logsDirectory(properties));
        server.setRemoteMachineURLs(remoteMachineNames(properties));
        server.setLocalBrowserFileNames(browserFileNames(properties));
        server.setTestURL(url(properties));
    }

    private URL url(Properties properties) {
        String url = properties.getProperty(PROPERTY_URL);
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL " + url);
        }
    }

    private List browserFileNames(Properties properties) {
        return Utility.listFromCommaDelimitedString(properties.getProperty(PROPERTY_BROWSER_FILE_NAMES));

    }

    private List remoteMachineNames(Properties properties) {
        String remoteMachineNamesCommaDelimitedList = properties.getProperty(PROPERTY_REMOTE_MACHINE_NAMES);
        return Utility.listFromCommaDelimitedString(remoteMachineNamesCommaDelimitedList);

    }

}