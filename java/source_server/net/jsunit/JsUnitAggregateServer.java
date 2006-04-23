package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsUnitAggregateServer extends AbstractJsUnitServer {

    private RemoteServerHitter hitter;
    private ArrayList<Configuration> cachedRemoteConfigurations;

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

    protected void preStart() {
        cachedRemoteConfigurations = new ArrayList<Configuration>();
        for (URL remoteMachineURL : configuration.getRemoteMachineURLs()) {
            RemoteConfigurationFetcher fetcher = new RemoteConfigurationFetcher(hitter, remoteMachineURL);
            fetcher.run();
            Configuration retrievedRemoteConfiguration = fetcher.getRetrievedRemoteConfiguration();
            if (retrievedRemoteConfiguration != null)
                cachedRemoteConfigurations.add(retrievedRemoteConfiguration);
        }
    }

    public List<Configuration> getCachedRemoteConfigurations() {
        return cachedRemoteConfigurations;
    }

}
