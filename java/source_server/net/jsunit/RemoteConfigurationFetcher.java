package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;

import java.net.URL;

public class RemoteConfigurationFetcher extends Thread implements Comparable<RemoteConfigurationFetcher> {
    private RemoteServerHitter hitter;
    private URL remoteMachineURL;
    private Configuration retrievedRemoteConfiguration;

    public RemoteConfigurationFetcher(RemoteServerHitter hitter, URL remoteMachineURL) {
        this.hitter = hitter;
        this.remoteMachineURL = remoteMachineURL;
    }

    public void run() {
        ConfigurationSource remoteSource = new RemoteConfigurationSource(hitter, remoteMachineURL.toString());
        retrievedRemoteConfiguration = new Configuration(remoteSource);
    }

    public URL getRemoteMachineURL() {
        return remoteMachineURL;
    }

    public Configuration getRetrievedRemoteConfiguration() {
        return retrievedRemoteConfiguration;
    }

    public int compareTo(RemoteConfigurationFetcher other) {
        return remoteMachineURL.toString().compareTo(other.getRemoteMachineURL().toString());
    }
}
