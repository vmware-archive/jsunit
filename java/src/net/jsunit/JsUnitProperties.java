package net.jsunit;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class JsUnitProperties extends Properties {

    public static String PROPERTY_PORT = "port";
    public static String PROPERTY_RESOURCE_BASE = "resourceBase";
    public static String PROPERTY_LOGS_DIRECTORY = "logsDirectory";

    public JsUnitProperties(String fileName) {
        try {
            load(new FileInputStream(fileName));
        } catch (Exception e) {
            throw new RuntimeException("Could not load " + fileName);
        }
    }

    public File resourceBase() {
        String result = getProperty(PROPERTY_RESOURCE_BASE);
        if (Utility.isEmpty(result))
            result = JsUnitServer.DEFAULT_RESOURCE_BASE;
        return new File(result);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(PROPERTY_PORT).append( ": ").append(port()).append("\n");
        result.append(PROPERTY_RESOURCE_BASE).append( ": ").append(resourceBase().getAbsolutePath()).append("\n");
        result.append(PROPERTY_LOGS_DIRECTORY).append( ": ").append(logsDirectory().getAbsolutePath());
        return result.toString();
    }

    public File logsDirectory() {
        String result = getProperty(PROPERTY_LOGS_DIRECTORY);
        if (Utility.isEmpty(result))
            result = resourceBase() + File.separator + "java" + File.separator + "logs";
        return new File(result);
    }

    public int port() {
        int result;
        String portString = getProperty(PROPERTY_PORT);
        if (Utility.isEmpty(portString))
            result = JsUnitServer.DEFAULT_PORT;
        else
            result = Integer.parseInt(portString);
        return result;
    }

}