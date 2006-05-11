package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.configuration.ServerType;
import net.jsunit.model.RemoteServerConfigurationSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ResourceHandler;
import org.apache.axis.transport.http.AxisServlet;
import org.apache.axis.transport.http.AdminServlet;
import org.apache.axis.transport.http.AxisHTTPSessionListener;
import org.apache.axis.monitor.SOAPMonitorService;
import org.apache.axis.components.logger.LogFactory;

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

    public RemoteServerHitter getHitter() {
        return hitter;
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
        setCachedRemoteConfigurations(result);
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

    protected void setUpHttpServer() throws Exception {
        super.setUpHttpServer();
        ServletHttpContext axisContext = new ServletHttpContext();
        axisContext.setContextPath("axis");
        axisContext.setResourceBase(configuration.getResourceBase().toString());
        axisContext.addServlet("AxisAdmin", "/admin", AdminServlet.class.getName());
        axisContext.addServlet("Axis", "/services/*", AxisServlet.class.getName());
        axisContext.setMimeMapping("wsdl", "text/xml");
        axisContext.setMimeMapping("xsd", "text/xml");
        axisContext.addWelcomeFile("services.html");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        axisContext.addHandler(resourceHandler);
        server.addContext(axisContext);
    }

    static class RemoteConfigurationCacheUpdater extends Thread {
        private static final long THREE_MINUTE_MILLIS = 1000 * 60 * 3;

        private JsUnitAggregateServer server;

        public RemoteConfigurationCacheUpdater(JsUnitAggregateServer server) {
            this.server = server;
        }

        public void run() {
            while (server.isAlive()) {
                try {
                    Thread.sleep(THREE_MINUTE_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                server.logStatus("Refreshing remote configurations for aggregate server");
                server.fetchRemoteConfigurations();
                server.logStatus("Done refreshing remote configurations for aggregate server");
            }
        }
    }

}
