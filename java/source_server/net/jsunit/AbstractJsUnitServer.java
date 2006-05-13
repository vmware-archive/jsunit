package net.jsunit;

import com.opensymphony.webwork.dispatcher.ServletDispatcher;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.ConfigurationProvider;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationException;
import net.jsunit.configuration.ConfigurationProperty;
import net.jsunit.configuration.ServerType;
import net.jsunit.results.Skin;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.XmlUtility;
import net.jsunit.version.VersionChecker;
import org.jdom.Element;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.SslListener;
import org.mortbay.http.handler.ForwardHandler;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.start.Monitor;
import org.mortbay.util.FileResource;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractJsUnitServer implements JsUnitServer, SkinSource {

    protected HttpServer server;
    private Logger logger = Logger.getLogger("net.jsunit");
    protected Configuration configuration;
    private final ServerType serverType;
    private Date startDate;
    protected int testRunCount = 0;
    private SkinSource skinSource = new DefaultSkinSource();
    private final List<StatusMessage> statusMessages = new ArrayList<StatusMessage>();
    public static final String PROPERTY_SSL_PASSWORD = "jsunitsslpassword";

    protected AbstractJsUnitServer(Configuration configuration, ServerType type) {
        this.configuration = configuration;
        this.serverType = type;
        ensureConfigurationIsValid();
        ServerRegistry.registerServer(this);
    }

    protected void ensureConfigurationIsValid() {
        if (!configuration.isValidFor(serverType)) {
            ConfigurationProperty property = serverType.getPropertiesInvalidFor(configuration).get(0);
            throw new ConfigurationException(property, property.getValueString(configuration));
        }
    }

    public boolean isAggregateServer() {
        return serverType.isAggregate();
    }

    public boolean isTemporary() {
        return serverType.isTemporary();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void start() throws Exception {
        preStart();
        setUpHttpServer();
        logStatus(startingServerStatusMessage());
        server.start();
        startDate = new Date();
        if (serverType.shouldPerformUpToDateCheck())
            performUpToDateCheck();
    }

    protected void preStart() {
    }

    private void performUpToDateCheck() {
        VersionChecker checker = VersionChecker.forDefault();
        if (!checker.isUpToDate())
            logger.warning(checker.outOfDateString());
    }

    private String startingServerStatusMessage() {
        return "Starting " +
                serverTypeName() +
                " Server with configuration:\r\n" +
                XmlUtility.asPrettyString(configuration.asXml(serverType));
    }

    protected String serverTypeName() {
        return serverType.getDisplayName();
    }

    protected void setUpHttpServer() throws Exception {
        FileResource.setCheckAliases(false);
        server = new HttpServer();
        setUpSocketListener();
        addJsUnitContext();
        setUpConfigurationProvider();
        setUpMonitor();
    }

    private void setUpMonitor() {
        if (Monitor.activeCount() == 0)
            Monitor.monitor();
    }

    private void setUpConfigurationProvider() {
        ConfigurationProvider provider = new ConfigurationProviderWithRunner(runnerActionName());

        ConfigurationManager.destroyConfiguration();
        ConfigurationManager.getConfigurationProviders().set(0, provider);
    }

    private void addJsUnitContext() throws Exception {
        ServletHttpContext jsunitContext = new ServletHttpContext();
        setUpJsUnitContext(jsunitContext);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        jsunitContext.addHandler(resourceHandler);

        jsunitContext.addHandler(new ForwardHandler("fragmentRunnerPage"));

        for (String servletName : servletNames())
            addWebworkServlet(jsunitContext, servletName);
        server.addContext(jsunitContext);
    }

    protected void setUpJsUnitContext(ServletHttpContext jsunitContext) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        jsunitContext.setContextPath("jsunit");
        jsunitContext.setResourceBase(configuration.getResourceBase().toString());
    }

    private void setUpSocketListener() {
        SocketListener listener;
        if (FileUtility.doesFileExist("java" + File.separator + "config" + File.separator + "keystore")) {
            SslListener sslListener = new SslListener();
            sslListener.setKeystore("java/config/keystore");
            sslListener.setPassword(System.getProperty(PROPERTY_SSL_PASSWORD));
            sslListener.setKeyPassword(System.getProperty(PROPERTY_SSL_PASSWORD));
            listener = sslListener;
        } else {
            listener = new SocketListener();
        }
        listener.setPort(configuration.getPort());
        server.addListener(listener);
    }

    protected List<String> servletNames() {
        List<String> result = new ArrayList<String>();
        result.add("acceptor");
        result.add("config");
        result.add("displayer");
        result.add("runner");
        result.add("serverstatus");
        result.add("testruncount");
        return result;
    }

    protected abstract String runnerActionName();

    private void addWebworkServlet(ServletHttpContext servletContext, String name) throws Exception {
        servletContext.addServlet(
                "webwork", "/" + name, ServletDispatcher.class.getName()
        );
    }

    public void logStatus(String message) {
        synchronized (statusMessages) {
            statusMessages.add(new StatusMessage(message));
            if (statusMessages.size() > 50) {
                int over50Count = statusMessages.size() - 50;
                for (int i = 0; i < over50Count; i++)
                    statusMessages.remove(0);
            }
        }
        logger.info(message);
    }

    public Element asXml() {
        return configuration.asXml(serverType);
    }

    public void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    public void dispose() {
        logStatus("Stopping JsUnit server");
        try {
            if (server != null)
                server.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isAlive() {
        return server != null && server.isStarted();
    }

    public Logger getLogger() {
        return logger;
    }

    public ServerType serverType() {
        return serverType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public long getTestRunCount() {
        return testRunCount;
    }

    public List<Skin> getSkins() {
        return skinSource.getSkins();
    }

    public Skin getSkinById(int skinId) {
        return skinSource.getSkinById(skinId);
    }

    public List<StatusMessage> getStatusMessages() {
        return new ArrayList<StatusMessage>(statusMessages);
    }

}