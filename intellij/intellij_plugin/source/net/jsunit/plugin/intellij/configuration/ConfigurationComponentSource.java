package net.jsunit.plugin.intellij.configuration;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.utility.SystemUtility;

class ConfigurationComponentSource implements ConfigurationSource {

    public String description() {
        return "IntelliJ plugin configuration source";
    }

    private ConfigurationComponent configurationComponent;

    public ConfigurationComponentSource(ConfigurationComponent configurationComponent) {
        this.configurationComponent = configurationComponent;
    }

    public String resourceBase() {
        return configurationComponent.getInstallationDirectory();
    }

    public String port() {
        return "8888";
    }

    public String logsDirectory() {
        return configurationComponent.getLogsDirectory();
    }

    public String browserFileNames() {
        StringBuffer buffer = new StringBuffer();
        String[] browserFileNames = configurationComponent.getBrowserFileNames();
        for (int i = 0; i < browserFileNames.length; i++) {
            buffer.append(browserFileNames[i]);
            if (i != (browserFileNames.length - 1))
                buffer.append(",");
        }
        return buffer.toString();
    }

    public String url() {
        return "";
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return null;
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

    public String closeBrowsersAfterTestRuns() {
        return String.valueOf(configurationComponent.isCloseBrowserAfterTestRuns());
    }

    public String logStatus() {
        return String.valueOf(configurationComponent.isLogStatusToConsole());
    }

    public String timeoutSeconds() {
        return String.valueOf(configurationComponent.getTimeoutSeconds());
    }

    public String remoteMachineURLs() {
        return "";
    }

}
