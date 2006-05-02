package net.jsunit.configuration;

import net.jsunit.utility.SystemUtility;

public class EnvironmentVariablesConfigurationSource implements ConfigurationSource {

    private String environmentVariableValue(ConfigurationProperty property) {
        return System.getProperty(property.getName());
    }

    public String resourceBase() {
        return environmentVariableValue(ConfigurationProperty.RESOURCE_BASE);
    }

    public String port() {
        return environmentVariableValue(ConfigurationProperty.PORT);
    }

    public String remoteMachineURLs() {
        return environmentVariableValue(ConfigurationProperty.REMOTE_MACHINE_URLS);
    }

    public String logsDirectory() {
        return environmentVariableValue(ConfigurationProperty.LOGS_DIRECTORY);
    }

    public String browserFileNames() {
        return environmentVariableValue(ConfigurationProperty.BROWSER_FILE_NAMES);
    }

    public String url() {
        return environmentVariableValue(ConfigurationProperty.URL);
    }

    public String closeBrowsersAfterTestRuns() {
        return environmentVariableValue(ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS);
    }

    public String description() {
        return environmentVariableValue(ConfigurationProperty.DESCRIPTION);
    }

    public String timeoutSeconds() {
        return environmentVariableValue(ConfigurationProperty.TIMEOUT_SECONDS);
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return environmentVariableValue(ConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES);
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

    public String useCaptcha() {
        return environmentVariableValue(ConfigurationProperty.USE_CAPTCHA);
    }

}