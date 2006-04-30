package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class FunctionalTestAggregateConfigurationSource implements ConfigurationSource {

    private int port;
    private int remotePort;

    public FunctionalTestAggregateConfigurationSource(int port, int remotePort) {
        this.port = port;
        this.remotePort = remotePort;
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

    public String trustedIpAddresses() {
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
        return "http://localhost:" + remotePort;
    }

}
