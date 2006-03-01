package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class DummyConfigurationSource implements ConfigurationSource {

    public static final String DUMMY_URL = "http://www.example.com:1234/jsunit/runner?autoRun=true&submitResults=true";
    public static final String REMOTE_URL_1 = "http://my.machine.com:8080/jsunit";
    public static final String REMOTE_URL_2 = "http://your.machine.com:9090/jsunit";

    public String resourceBase() {
        return ".";
    }

    public String port() {
        return "1234";
    }

    public String logsDirectory() {
        return "";
    }

    public String browserFileNames() {
        return "dummy browser filename";
    }

    public String url() {
        return DUMMY_URL;
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return "";
    }

    public String closeBrowsersAfterTestRuns() {
        return "true";
    }

    public String description() {
        return "This is my server!";
    }

    public String logStatus() {
        return "false";
    }

    public String timeoutSeconds() {
        return "60";
    }

    public String remoteMachineURLs() {
        return REMOTE_URL_1 + "," + REMOTE_URL_2;
    }

}