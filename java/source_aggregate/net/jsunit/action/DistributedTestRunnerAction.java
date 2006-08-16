package net.jsunit.action;

import com.opensymphony.xwork.ActionSupport;
import net.jsunit.*;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.server.RemoteRunSpecificationBuilder;

import java.util.List;
import java.util.logging.Logger;

public class DistributedTestRunnerAction
        extends ActionSupport
        implements RequestSourceAware,
        TestPageURLAware,
        XmlProducer,
        RemoteRunnerHitterAware,
        RemoteRunSpecificationAware,
        JsUnitAggregateServerAware {

    private JsUnitAggregateServer aggregateServer;
    protected DistributedTestRunManager manager;
    protected String overrideURL;
    protected String remoteIpAddress;
    protected String remoteHost;
    protected String referrer;
    protected RemoteServerHitter hitter;
    protected List<RemoteRunSpecification> remoteRunSpecs;
    private String errorMessage;
    private static final Logger logger = Logger.getLogger("net.jsunit");

    public void setRemoteServerHitter(RemoteServerHitter hitter) {
        this.hitter = hitter;
    }

    public String execute() throws Exception {
        if (errorMessage == null) {
            long startTime = System.currentTimeMillis();
            logger.info(new RequestReceivedMessage(remoteHost, remoteIpAddress, overrideURL).generateMessage());
            //noinspection SynchronizeOnNonFinalField
            synchronized (gateKeeper()) {
                runTests();
            }
            logger.info("Done running aggregate tests ( " + ((System.currentTimeMillis() - startTime) / 1000d) + " seconds)");
        }
        return SUCCESS;
    }

    protected Object gateKeeper() {
        return aggregateServer;
    }

    private void runTests() {
        createTestRunManager();
        manager.runTests();
    }

    protected void createTestRunManager() {
        this.manager = new DistributedTestRunManager(hitter, getConfiguration(), overrideURL, remoteRunSpecs);
    }

    public XmlRenderable getXmlRenderable() {
        if (errorMessage != null)
            return new ErrorXmlRenderable(errorMessage);
        else
            return manager.getDistributedTestRunResult();
    }

    public DistributedTestRunManager getTestRunManager() {
        return manager;
    }

    public void setUrl(String overrideURL) {
        this.overrideURL = overrideURL.trim();
    }

    public void setRequestIPAddress(String ipAddress) {
        this.remoteIpAddress = ipAddress;
    }

    public void setRequestHost(String host) {
        this.remoteHost = host;
    }

    public String getRequestIpAddress() {
        return this.remoteIpAddress;
    }

    public String getReferrer() {
        return this.referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public void setRemoteRunSpecifications(List<RemoteRunSpecification> remoteRunSpecs) {
        this.remoteRunSpecs = remoteRunSpecs;
    }

    public RemoteConfiguration getRemoteMachineConfigurationById(int id) {
        return remoteMachineConfigurationCache().getRemoteMachineConfigurationById(id);
    }

    public List<RemoteConfiguration> getAllRemoteMachineConfigurations() {
        return remoteMachineConfigurationCache().getAllRemoteMachineConfigurations();
    }

    protected RemoteConfigurationCache remoteMachineConfigurationCache() {
        return aggregateServer;
    }

    public Configuration getConfiguration() {
        return aggregateServer.getConfiguration();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setAggregateServer(JsUnitAggregateServer aggregateServer) {
        this.aggregateServer = aggregateServer;
        List<RemoteConfiguration> remoteMachineConfigurations = aggregateServer.getAllRemoteMachineConfigurations();
        remoteRunSpecs = new RemoteRunSpecificationBuilder().forAllBrowsersFromRemoteConfigurations(remoteMachineConfigurations);
    }

    public JsUnitAggregateServer getAggregateServer() {
        return aggregateServer;
    }
}
