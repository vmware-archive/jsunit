package net.jsunit;

import java.io.File;
import java.util.List;
import java.net.URL;
import java.net.MalformedURLException;

public abstract class AbstractConfiguration {

    public static final String PORT = "port";
    public static final String RESOURCE_BASE = "resourceBase";
    public static final String LOGS_DIRECTORY = "logsDirectory";
    public static final String REMOTE_MACHINE_NAMES = "remoteMachineNames";
    public static final String URL = "url";
    public static final String BROWSER_FILE_NAMES = "browserFileNames";

    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_RESOURCE_BASE = ".";

    public void configure(JsUnitServer server) {
        String resourceBase = resourceBase();
        if (Utility.isEmpty(resourceBase))
            resourceBase = DEFAULT_RESOURCE_BASE;
        server.setResourceBase(new File(resourceBase));

        int port;
        String portString = port();
        if (Utility.isEmpty(portString))
            port = DEFAULT_PORT;
        else
            port = Integer.parseInt(portString);
        server.setPort(port);

        String logsDirectory = logsDirectory();
        if (Utility.isEmpty(logsDirectory))
            logsDirectory = resourceBase() + File.separator + "java" + File.separator + "logs";
        server.setLogsDirectory(new File(logsDirectory));

        String remoteMachineURLsCommaDelimitedList = remoteMachineNames();
        List names = Utility.listFromCommaDelimitedString(remoteMachineURLsCommaDelimitedList);
        server.setRemoteMachineNames(names);

        List browserFileNames = Utility.listFromCommaDelimitedString(browserFileNames());
        server.setLocalBrowserFileNames(browserFileNames);

        String urlString = url();
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL " + url);
        }
        server.setTestURL(url);
    }

    protected abstract String resourceBase();

    protected abstract String port();

    protected abstract String logsDirectory();

    protected abstract String remoteMachineNames();

    protected abstract String browserFileNames();

    protected abstract String url();
}
