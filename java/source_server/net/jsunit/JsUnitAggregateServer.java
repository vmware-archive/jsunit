package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.configuration.ServerType;
import net.jsunit.model.RemoteServerConfigurationSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsUnitAggregateServer extends AbstractJsUnitServer implements RemoteServerConfigurationSource {

    private RemoteServerHitter hitter;
    private List<RemoteConfiguration> cachedRemoteConfigurations;
    private JsUnitAggregateServer.RemoteConfigurationCacheUpdater updater;

    public JsUnitAggregateServer(Configuration configuration) {
        this(configuration, new RemoteMachineServerHitter());
    }

    public JsUnitAggregateServer(Configuration configuration, RemoteServerHitter hitter) {
        super(configuration, ServerType.AGGREGATE);
        this.hitter = hitter;
    }

    public static void main(String args[]) {
        try {
            JsUnitAggregateServer server = new JsUnitAggregateServer(Configuration.resolve(args));
            server.start();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String toString() {
        return "JsUnit Aggregate Server";
    }

    protected String runnerActionName() {
        return "distributedTestRunner";
    }

    public ServerType serverType() {
        return ServerType.AGGREGATE;
    }

    public void finishedTestRun() {
        testRunCount++;
    }

    protected synchronized void preStart() {
        fetchRemoteConfigurations();
        updater = new RemoteConfigurationCacheUpdater(this);
        updater.start();
    }

    private synchronized void fetchRemoteConfigurations() {
        List<RemoteConfiguration> result = new ArrayList<RemoteConfiguration>();
        for (URL remoteMachineURL : configuration.getRemoteMachineURLs()) {
            RemoteConfigurationFetcher fetcher = new RemoteConfigurationFetcher(hitter, remoteMachineURL);
            fetcher.run();
            RemoteConfiguration retrievedRemoteConfiguration = fetcher.getRetrievedRemoteConfiguration();
            if (retrievedRemoteConfiguration != null)
                result.add(retrievedRemoteConfiguration);
        }
        cachedRemoteConfigurations = result;
    }

    public List<RemoteConfiguration> getCachedRemoteConfigurations() {
        return cachedRemoteConfigurations;
    }

    public RemoteConfiguration getRemoteMachineConfigurationById(int id) {
        return cachedRemoteConfigurations.get(id);
    }

    public List<RemoteConfiguration> getAllRemoteMachineConfigurations() {
        return cachedRemoteConfigurations;
    }

    static class RemoteConfigurationCacheUpdater extends Thread {
        private static final long ONE_MINUTE_MILLIS = 1000 * 60;

        private JsUnitAggregateServer server;

        public RemoteConfigurationCacheUpdater(JsUnitAggregateServer server) {
            this.server = server;
        }

        public void run() {
            while (server.isAlive()) {
                try {
                    Thread.sleep(ONE_MINUTE_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                server.fetchRemoteConfigurations();
            }
        }
    }

}
