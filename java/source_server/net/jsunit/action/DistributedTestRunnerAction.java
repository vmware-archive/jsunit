package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.*;
import net.jsunit.configuration.Configuration;
import net.jsunit.results.Skin;

public class DistributedTestRunnerAction implements
        RequestSourceAware,
        SkinAware,
        TestPageURLAware,
        Action,
        XmlProducer,
        RemoteRunnerHitterAware,
        JsUnitAggregateServerAware {

    public static final String TRANSFORM = "transform";

    private DistributedTestRunManager manager;
    private String overrideURL;
    private Skin skin;
    private String remoteIpAddress;
    private String remoteHost;
    protected JsUnitAggregateServer server;
    protected RemoteServerHitter hitter;

    public void setAggregateServer(JsUnitAggregateServer server) {
        this.server = server;
    }

    public void setRemoteServerHitter(RemoteServerHitter hitter) {
        this.hitter = hitter;
    }

    public String execute() throws Exception {
        String message = new RequestReceivedMessage(remoteHost, remoteIpAddress, overrideURL).generateMessage();
        server.logStatus(message);
        //noinspection SynchronizeOnNonFinalField
        synchronized (server) {
            manager = DistributedTestRunManager.forMultipleRemoteMachines(
                    hitter, server.getConfiguration(), server.getConfiguration().getRemoteMachineURLs(), overrideURL
            );
            manager.runTests();
        }
        server.finishedTestRun();
        server.logStatus("Done running aggregate tests");
        return skin != null ? TRANSFORM : SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        return manager.getDistributedTestRunResult();
    }

    public DistributedTestRunManager getTestRunManager() {
        return manager;
    }

    public void setUrl(String overrideURL) {
        this.overrideURL = overrideURL;
    }

    public Configuration getServerConfiguration() {
        return server.getConfiguration();
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin() {
        return skin;
    }

    public SkinSource getSkinSource() {
        return server;
    }

    public void setRequestIPAddress(String ipAddress) {
        this.remoteIpAddress = ipAddress;
    }

    public void setRequestHost(String host) {
        this.remoteHost = host;
    }

}
