package net.jsunit;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public abstract class JsUnitConfiguration {

    public static final String PORT = "port";
    public static final String RESOURCE_BASE = "resourceBase";
    public static final String LOGS_DIRECTORY = "logsDirectory";
    public static final String URL = "url";
    public static final String BROWSER_FILE_NAMES = "browserFileNames";

    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_RESOURCE_BASE = ".";

    public static void configure(JsUnitServer server, String[] args) throws ConfigurationException {
        if (args.length == 0)
            new PropertiesConfiguration().doConfigure(server);
        else
            new ArgumentsConfiguration(Arrays.asList(args)).doConfigure(server);
    }

    public void doConfigure(JsUnitServer server) throws ConfigurationException {
        configureResourceBase(server);
        configurePort(server);
        configureLogsDirectory(server);
        configureBrowserFileNames(server);
        configureTestURL(server);
    }

    private void configureTestURL(JsUnitServer server) throws ConfigurationException {
        try {
            String urlString = url();
            server.setTestURL(new URL(urlString));
        } catch (Exception e) {
            throw new ConfigurationException(URL, e);
        }
    }

    private void configureBrowserFileNames(JsUnitServer server) throws ConfigurationException {
        try {
            List browserFileNames = Utility.listFromCommaDelimitedString(browserFileNames());
            server.setLocalBrowserFileNames(browserFileNames);
        } catch (Exception e) {
            throw new ConfigurationException(BROWSER_FILE_NAMES, e);
        }
    }

    private void configureLogsDirectory(JsUnitServer server) throws ConfigurationException {
        try {
            String logsDirectoryString = logsDirectory();
            if (Utility.isEmpty(logsDirectoryString))
                logsDirectoryString = resourceBase() + "logs";
            File logsDirectory = new File(logsDirectoryString);
            if (!logsDirectory.exists()) {
                Utility.log("Creating logs directory " + logsDirectory.getAbsolutePath(), false);
                logsDirectory.mkdir();
            }
            server.setLogsDirectory(logsDirectory);
        } catch (Exception e) {
            throw new ConfigurationException(LOGS_DIRECTORY, e);
        }
    }

    private void configurePort(JsUnitServer server) throws ConfigurationException {
        try {
            int port = 0;
            String portString = port();
            if (Utility.isEmpty(portString))
                port = DEFAULT_PORT;
            else
                port = Integer.parseInt(portString);
            server.setPort(port);
        } catch (Exception e) {
            throw new ConfigurationException(PORT, e);
        }
    }

    private void configureResourceBase(JsUnitServer server) throws ConfigurationException {
        try {
            String resourceBase = resourceBase();
            if (Utility.isEmpty(resourceBase))
                resourceBase = DEFAULT_RESOURCE_BASE;
            server.setResourceBase(new File(resourceBase));
        } catch (Exception e) {
            throw new ConfigurationException(RESOURCE_BASE, e);
        }
    }

    protected abstract String resourceBase();

    protected abstract String port();

    protected abstract String logsDirectory();

    protected abstract String browserFileNames();

    protected abstract String url();
}