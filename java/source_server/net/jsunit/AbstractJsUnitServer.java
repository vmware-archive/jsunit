package net.jsunit;

import com.opensymphony.webwork.dispatcher.ServletDispatcher;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.ConfigurationProvider;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationException;
import net.jsunit.configuration.ConfigurationProperty;
import net.jsunit.configuration.ServerType;
import net.jsunit.results.Skin;
import net.jsunit.utility.XmlUtility;
import net.jsunit.version.VersionChecker;
import org.apache.jasper.servlet.JspServlet;
import org.jdom.Element;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ForwardHandler;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.start.Monitor;
import org.mortbay.util.FileResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractJsUnitServer implements JsUnitServer, SkinSource {

    private HttpServer server;
    private Logger logger = Logger.getLogger("net.jsunit");
    protected Configuration configuration;
    private final ServerType serverType;
    private Date startDate;
    protected int testRunCount = 0;
    private SkinSource skinSource = new DefaultSkinSource();
    private final List<StatusMessage> statusMessages = new ArrayList<StatusMessage>();

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
        setUpHttpServer();
        logStatus(startingServerStatusMessage());
        server.start();
        startDate = new Date();
        if (serverType.shouldPerformUpToDateCheck())
            performUpToDateCheck();
        postStart();
    }

    protected void postStart() {
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

    private void setUpHttpServer() throws Exception {
        FileResource.setCheckAliases(false);
        server = new HttpServer();
        SocketListener listener = new SocketListener();
        listener.setPort(configuration.getPort());
        server.addListener(listener);

        ServletHttpContext rootContext = new ServletHttpContext();
        rootContext.setContextPath("/");
        rootContext.addHandler(new ForwardHandler("/jsunit"));
        server.addContext(rootContext);

        ServletHttpContext jsunitContext = new ServletHttpContext();
        jsunitContext.setContextPath("jsunit");
        jsunitContext.setResourceBase(configuration.getResourceBase().toString());
        jsunitContext.addWelcomeFile("java/jsp/fragmentRunner.jsp");
        jsunitContext.addServlet("JSP", "*.jsp", JspServlet.class.getName());

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        jsunitContext.addHandler(resourceHandler);

        jsunitContext.addHandler(new ForwardHandler("fragmentRunnerPage"));

        for (String servletName : servletNames())
            addWebworkServlet(jsunitContext, servletName);
        server.addContext(jsunitContext);

        ConfigurationProvider provider = new ConfigurationProviderWithRunner(runnerActionName());

        ConfigurationManager.clearConfigurationProviders();
        ConfigurationManager.addConfigurationProvider(provider);

        if (Monitor.activeCount() == 0)
            Monitor.monitor();
    }

    protected List<String> servletNames() {
        return Arrays.asList(new String[]{
                "fragmentRunnerPage",
                "uploadRunnerPage",
                "urlRunnerPage",
                "logDisplayerPage",
                "configurationPage",

                "acceptor",
                "admin",
                "config",
                "displayer",
                "latestversion",
                "runner",
                "serverstatus",
                "testruncount"
        });
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