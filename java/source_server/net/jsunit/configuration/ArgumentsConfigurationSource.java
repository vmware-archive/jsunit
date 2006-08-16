package net.jsunit.configuration;

import net.jsunit.utility.SystemUtility;

import java.util.List;

public class ArgumentsConfigurationSource implements ConfigurationSource {

    private List<String> arguments;

    public ArgumentsConfigurationSource(List<String> arguments) {
        this.arguments = arguments;
    }

    protected String argumentValue(String propertyName) {
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i).equalsIgnoreCase("-" + propertyName)) {
                String value = arguments.get(i + 1);
                if (!value.startsWith("-"))
                    return value;
                else
                    return "";
            }
        }
        return null;
    }

    public String resourceBase() {
        return argumentValue(ServerConfigurationProperty.RESOURCE_BASE.getName());
    }

    public String port() {
        return argumentValue(ServerConfigurationProperty.PORT.getName());
    }

    public String remoteMachineURLs() {
        return argumentValue(ServerConfigurationProperty.REMOTE_MACHINE_URLS.getName());
    }

    public String logsDirectory() {
        return argumentValue(ServerConfigurationProperty.LOGS_DIRECTORY.getName());
    }

    public String browserFileNames() {
        return argumentValue(ServerConfigurationProperty.BROWSER_FILE_NAMES.getName());
    }

    public String url() {
        return argumentValue(ServerConfigurationProperty.URL.getName());
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return argumentValue(ServerConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES.getName());
    }

    public String closeBrowsersAfterTestRuns() {
        return argumentValue(ServerConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName());
    }

    public String description() {
        return argumentValue(ServerConfigurationProperty.DESCRIPTION.getName());
    }

    public String timeoutSeconds() {
        return argumentValue(ServerConfigurationProperty.TIMEOUT_SECONDS.getName());
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