package net.jsunit;

import com.opensymphony.xwork.config.ConfigurationProvider;
import net.jsunit.configuration.*;
import net.jsunit.model.PlatformType;
import net.jsunit.model.RemoteServerConfigurationSource;

import java.util.ArrayList;
import java.util.List;

public class JsUnitAggregateServer
        extends AbstractJsUnitServer
        implements RemoteServerConfigurationSource, RemoteConfigurationCache {

    private static JsUnitAggregateServer instance;

    private RemoteServerHitter hitter;
    private List<RemoteConfiguration> cachedRemoteConfigurations = new ArrayList<RemoteConfiguration>();
    private RemoteConfigurationCacheUpdater remoteConfigurationCacheUpdater;

    public JsUnitAggregateServer(AggregateConfiguration configuration) {
        this(configuration, new RemoteMachineServerHitter());
    }

    public JsUnitAggregateServer(AggregateConfiguration configuration, RemoteServerHitter hitter) {
        super(configuration);
        setHitter(hitter);
        registerInstance(this);
        remoteConfigurationCacheUpdater = new RemoteConfigurationCacheUpdater(this, configuration.getRemoteMachineURLs(), hitter);
    }

    public void setHitter(RemoteServerHitter hitter) {
        this.hitter = hitter;
    }

    public RemoteServerHitter getHitter() {
        return hitter;
    }

    public static void main(String args[]) {
        try {
            ConfigurationSource source = CompositeConfigurationSource.forArguments(args);
            JsUnitAggregateServer server = new JsUnitAggregateServer(new AggregateConfiguration(source));
            server.start();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String toString() {
        return "JsUnit Aggregate Server";
    }

    public ServerType serverType() {
        return ServerType.AGGREGATE;
    }

    protected void preStart() {
        super.preStart();
        remoteConfigurationCacheUpdater.fetch();
    }

    protected void postStart() {
        super.postStart();
        remoteConfigurationCacheUpdater.start();
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

    public synchronized void setCachedRemoteConfigurations(List<RemoteConfiguration> cachedRemoteConfigurations) {
        this.cachedRemoteConfigurations = cachedRemoteConfigurations;
    }

    protected ConfigurationProvider createConfigurationProvider() {
        return new JsUnitAggregateServerConfigurationProvider();
    }

    protected String resourceBase() {
        return ".";
    }

    public PlatformType getPlatformType() {
        return PlatformType.resolve();
    }

    protected List<String> servletNames() {
        List<String> result = new ArrayList<String>();
        result.add("config");
        result.add("runner");
        return result;
    }

    public void dispose() {
        super.dispose();
        remoteConfigurationCacheUpdater.stopFetching();
    }

    public static void registerInstance(JsUnitAggregateServer instance) {
        JsUnitAggregateServer.instance = instance;
    }

    public static JsUnitAggregateServer instance() {
        return instance;
    }

}
