package net.jsunit;

import net.jsunit.configuration.RemoteConfiguration;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class RemoteConfigurationFetcher extends Thread implements Comparable<RemoteConfigurationFetcher> {

    private Logger logger = Logger.getLogger("net.jsunit");

    private RemoteServerHitter hitter;
    private URL remoteMachineURL;
    private RemoteConfiguration retrievedRemoteConfiguration;

    public RemoteConfigurationFetcher(RemoteServerHitter hitter, URL remoteMachineURL) {
        this.hitter = hitter;
        this.remoteMachineURL = remoteMachineURL;
    }

    public void run() {
        try {
            fetch();
        } catch (IOException e) {
            logger.severe("Cannot retrieve remote configuration on " + remoteMachineURL.toString() + ": " + e.getMessage());
        }
    }

    public void fetch() throws IOException {
        logger.info("Fetching remote machine configuration from " + remoteMachineURL.toString());
        RemoteConfigurationSource remoteSource = new RemoteConfigurationSource(hitter, remoteMachineURL.toString());
        retrievedRemoteConfiguration = new RemoteConfiguration(remoteMachineURL, remoteSource);
        logger.info("Done fetching remote machine configuration from " + remoteMachineURL.toString());
    }

    public URL getRemoteMachineURL() {
        return remoteMachineURL;
    }

    public RemoteConfiguration getRetrievedRemoteConfiguration() {
        return retrievedRemoteConfiguration;
    }

    public int compareTo(RemoteConfigurationFetcher other) {
        return remoteMachineURL.toString().compareTo(other.getRemoteMachineURL().toString());
    }
}
