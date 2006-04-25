package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class DummyAggregateConfigurationSource implements ConfigurationSource {
    public static final String REMOTE_URL_1 = "http://www.example.com:8081";
    public static final String REMOTE_URL_2 = "http://www.example.com:8082";

    public String resourceBase() {
        return ".";
    }

    public String port() {
        return String.valueOf(new TestPortManager().newPort());
    }

    public String logsDirectory() {
        return "c:\\logs";
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

    public String closeBrowsersAfterTestRuns() {
        return null;
    }

    public String description() {
        return "This is a cool server";
    }

    public String logStatus() {
        return "true";
    }

    public String timeoutSeconds() {
        return "25";
    }

    public String remoteMachineURLs() {
        return REMOTE_URL_1 + "," + REMOTE_URL_2;
    }

}
