package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class RemoteConfigurationFetcher extends Thread implements Comparable<RemoteConfigurationFetcher> {

    private Logger logger = Logger.getLogger("net.jsunit");

    private RemoteServerHitter hitter;
    private URL remoteMachineURL;
    private Configuration retrievedRemoteConfiguration;

    public RemoteConfigurationFetcher(RemoteServerHitter hitter, URL remoteMachineURL) {
        this.hitter = hitter;
        this.remoteMachineURL = remoteMachineURL;
    }

    public void run() {
        try {
            ConfigurationSource remoteSource = new RemoteConfigurationSource(hitter, remoteMachineURL.toString());
            retrievedRemoteConfiguration = new Configuration(remoteSource);
        } catch (IOException e) {
            logger.severe("Cannot retrieve remote configuration: " + e.getMessage());
        }
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
