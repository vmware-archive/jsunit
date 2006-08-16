package net.jsunit.configuration;

import net.jsunit.MockRemoteServerHitter;
import net.jsunit.RemoteConfigurationSource;

import java.io.IOException;

public class DummyRemoteConfigurationSource extends RemoteConfigurationSource {
    private DummyConfigurationSource source;

    public DummyRemoteConfigurationSource(String remoteMachineURL) throws IOException {
        super(new MockRemoteServerHitter(), remoteMachineURL);
        this.source = new DummyConfigurationSource();
    }

    public String browserFileNames() {
        return source.browserFileNames();
    }

    public String closeBrowsersAfterTestRuns() {
        return source.closeBrowsersAfterTestRuns();
    }

    public String description() {
        return source.description();
    }

    public String hostname() {
        return source.hostname();
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return source.ignoreUnresponsiveRemoteMachines();
    }

    public String ipAddress() {
        return source.ipAddress();
    }

    public String logsDirectory() {
        return source.logsDirectory();
    }

    public String logStatus() {
        return source.logStatus();
    }

    public String osString() {
        return source.osString();
    }

    public String port() {
        return source.port();
    }

    public String remoteMachineURLs() {
        return source.remoteMachineURLs();
    }

    public String resourceBase() {
        return source.resourceBase();
    }

    public void setNeeds3rdRemoteMachineURL() {
        source.setNeeds3rdRemoteMachineURL();
    }

    public String timeoutSeconds() {
        return source.timeoutSeconds();
    }

    public String url() {
        return source.url();
    }


}
