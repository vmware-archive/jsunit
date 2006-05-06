package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class FunctionalTestAggregateConfigurationSource implements ConfigurationSource {

    private int port;
    public static final String REMOTE_SERVER_URL_1 = "http://server1.mycompany.com:1234/jsunit";
    public static final String REMOTE_SERVER_URL_2 = "http://server2.mycompany.com:5678/jsunit";

    public FunctionalTestAggregateConfigurationSource(int port) {
        this.port = port;
    }

    public String resourceBase() {
        return null;
    }

    public String port() {
        return String.valueOf(port);
    }

    public String logsDirectory() {
        return ".";
    }

    public String browserFileNames() {
        return null;
    }

    public String url() {
        return null;
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return null;
    }

    public String osString() {
        return null;
    }

    public String ipAddress() {
        return null;
    }

    public String hostname() {
        return null;
    }

    public String useCaptcha() {
        return null;
    }

    public String closeBrowsersAfterTestRuns() {
        return null;
    }

    public String description() {
        return null;
    }

    public String logStatus() {
        return "true";
    }

    public String timeoutSeconds() {
        return "60";
    }

    public String remoteMachineURLs() {
        return REMOTE_SERVER_URL_1 + "," + REMOTE_SERVER_URL_2;
    }

}
