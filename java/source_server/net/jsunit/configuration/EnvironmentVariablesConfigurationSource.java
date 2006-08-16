package net.jsunit.configuration;

import net.jsunit.utility.SystemUtility;

public class EnvironmentVariablesConfigurationSource implements ConfigurationSource {

    protected String environmentVariableValue(String propertyName) {
        return System.getProperty(propertyName);
    }

    public String resourceBase() {
        return environmentVariableValue(ServerConfigurationProperty.RESOURCE_BASE.getName());
    }

    public String port() {
        return environmentVariableValue(ServerConfigurationProperty.PORT.getName());
    }

    public String remoteMachineURLs() {
        return environmentVariableValue(ServerConfigurationProperty.REMOTE_MACHINE_URLS.getName());
    }

    public String logsDirectory() {
        return environmentVariableValue(ServerConfigurationProperty.LOGS_DIRECTORY.getName());
    }

    public String browserFileNames() {
        return environmentVariableValue(ServerConfigurationProperty.BROWSER_FILE_NAMES.getName());
    }

    public String url() {
        return environmentVariableValue(ServerConfigurationProperty.URL.getName());
    }

    public String closeBrowsersAfterTestRuns() {
        return environmentVariableValue(ServerConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName());
    }

    public String description() {
        return environmentVariableValue(ServerConfigurationProperty.DESCRIPTION.getName());
    }

    public String timeoutSeconds() {
        return environmentVariableValue(ServerConfigurationProperty.TIMEOUT_SECONDS.getName());
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return environmentVariableValue(ServerConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES.getName());
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