package net.jsunit;

import com.opensymphony.webwork.dispatcher.ServletDispatcher;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.ConfigurationProvider;
import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.configuration.ServerType;
import net.jsunit.utility.XmlUtility;
import org.jdom.Element;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.start.Monitor;
import org.mortbay.util.FileResource;

import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractJsUnitServer {

    protected Server httpServer;
    protected Logger logger = Logger.getLogger("net.jsunit");
    protected ServerConfiguration configuration;
    private Object originalActionExtension;
    private static final String WEBWORK_ACTION_EXTENSION = "webwork.action.extension";

    protected AbstractJsUnitServer(ServerConfiguration configuration) {
        setConfiguration(configuration);
    }

    private void setConfiguration(ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start() throws Exception {
        preStart();
        setUpHttpServer();
        logger.info(startingServerStatusMessage());
        httpServer.start();
        postStart();
    }

    protected void preStart() {
        originalActionExtension = com.opensymphony.webwork.config.Configuration.get(WEBWORK_ACTION_EXTENSION);
        com.opensymphony.webwork.config.Configuration.set(WEBWORK_ACTION_EXTENSION, "");
    }

    protected void postStart() {
    }

    private String startingServerStatusMessage() {
        return "Starting " +
                serverTypeName() +
                " Server with configuration:\r\n" +
                XmlUtility.asPrettyString(configuration.asXml());
    }

    protected String serverTypeName() {
        return configuration.getServerType().getDisplayName();
    }

    protected void setUpHttpServer() throws Exception {
        FileResource.setCheckAliases(false);
        httpServer = new Server();
        setUpSocketListener();
        addServerContext();
        setUpConfigurationProvider();
        setUpMonitor();
    }

    private void setUpMonitor() {
        if (Monitor.activeCount() == 0)
            Monitor.monitor();
    }

    private void setUpConfigurationProvider() {
        ConfigurationProvider provider = createConfigurationProvider();

        ConfigurationManager.destroyConfiguration();
        //noinspection unchecked
        ConfigurationManager.getConfigurationProviders().set(0, provider);
    }

    protected abstract ConfigurationProvider createConfigurationProvider();

    protected void addServerContext() throws Exception {
        ServletHttpContext jsunitContext = new ServletHttpContext();
        jsunitContext.setContextPath("jsunit");
        jsunitContext.setResourceBase(resourceBase());
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        jsunitContext.addHandler(resourceHandler);
        for (String servletName : servletNames())
            jsunitContext.addServlet("webwork", "/" + servletName, ServletDispatcher.class.getName());
        httpServer.addContext(jsunitContext);
    }

    protected abstract String resourceBase();

    protected abstract List<String> servletNames();

    private void setUpSocketListener() {
        SocketListener listener = new SocketListener();
        listener.setPort(configuration.getPort());
        httpServer.addListener(listener);
    }

    public Element asXml() {
        return configuration.asXml();
    }

    public void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    public void dispose() {
        com.opensymphony.webwork.config.Configuration.set(WEBWORK_ACTION_EXTENSION, originalActionExtension);
        logger.info("Stopping JsUnit Server");
        try {
            if (httpServer != null)
                httpServer.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isAlive() {
        return httpServer != null && httpServer.isStarted();
    }

    public ServerType serverType() {
        return configuration.getServerType();
    }

    public ServerConfiguration getConfiguration() {
        return configuration;
    }
}