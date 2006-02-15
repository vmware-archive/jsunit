package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class FunctionalTestFarmConfigurationSource implements ConfigurationSource {

    private int[] ports;

    public FunctionalTestFarmConfigurationSource(int... ports) {
        this.ports = ports;
    }

    public String resourceBase() {
        return null;
    }

    public String port() {
        return String.valueOf(FunctionalTestCase.PORT);
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

    public String closeBrowsersAfterTestRuns() {
        return null;
    }

    public String logStatus() {
        return "true";
    }

    public String timeoutSeconds() {
        return "60";
    }

    public String remoteMachineURLs() {
        StringBuffer buffer = new StringBuffer();
        for (int port : ports) {
            buffer.append("http://localhost:");
            buffer.append(port);
        }
        return buffer.toString();
    }

}
