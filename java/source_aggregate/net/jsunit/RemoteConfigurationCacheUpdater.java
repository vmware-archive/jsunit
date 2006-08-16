package net.jsunit;

import net.jsunit.configuration.RemoteConfiguration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RemoteConfigurationCacheUpdater extends Thread {
    private static final long THREE_MINUTE_MILLIS = 1000 * 60 * 3;

    private static final Logger logger = Logger.getLogger("net.jsunit");

    private RemoteConfigurationCache cache;
    private List<URL> remoteMachineURLs;
    private RemoteServerHitter hitter;
    private boolean running;

    public RemoteConfigurationCacheUpdater(RemoteConfigurationCache cache, List<URL> remoteMachineURLs, RemoteServerHitter hitter) {
        this.cache = cache;
        this.remoteMachineURLs = remoteMachineURLs;
        this.hitter = hitter;
        setDaemon(true);
    }

    public synchronized void start() {
        running = true;
        super.start();
    }

    public void run() {
        while (running) {
            try {
                Thread.sleep(THREE_MINUTE_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fetch();
        }
    }

    public void fetch() {
        logger.info("Fetching remote machine configurations (" + remoteMachineURLs.size() + " remote servers)");
        List<RemoteConfiguration> result = new ArrayList<RemoteConfiguration>();
        for (URL remoteMachineURL : remoteMachineURLs) {
            RemoteConfigurationFetcher fetcher = new RemoteConfigurationFetcher(hitter, remoteMachineURL);
            try {
                fetcher.fetch();
            } catch (IOException e) {
                logger.severe("Could not fetch remote configuration for URL " + remoteMachineURL.toString());
            }
            RemoteConfiguration retrievedRemoteConfiguration = fetcher.getRetrievedRemoteConfiguration();
            if (retrievedRemoteConfiguration != null)
                result.add(retrievedRemoteConfiguration);
        }
        cache.setCachedRemoteConfigurations(result);
        logger.info("Done fetching remote machine configurations");
    }

    public void stopFetching() {
        running = false;
    }
}
