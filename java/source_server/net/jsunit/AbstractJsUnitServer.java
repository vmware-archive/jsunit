package net.jsunit;

import com.opensymphony.webwork.dispatcher.ServletDispatcher;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationException;
import net.jsunit.configuration.ConfigurationProperty;
import net.jsunit.configuration.ServerType;
import net.jsunit.logging.JsUnitLogger;
import net.jsunit.logging.NoOpJsUnitLogger;
import net.jsunit.logging.SystemOutJsUnitLogger;
import net.jsunit.utility.XmlUtility;
import net.jsunit.version.VersionChecker;
import org.apache.jasper.servlet.JspServlet;
import org.jdom.Element;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.start.Monitor;

import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractJsUnitServer implements JsUnitServer {

    private HttpServer server;
    private JsUnitLogger jsUnitLogger;
    protected Configuration configuration;

    protected AbstractJsUnitServer(Configuration configuration) {
        this.configuration = configuration;
        ensureConfigurationIsValid();
        setUpLogsDirectory(configuration);
        setUpLogger(configuration);
        setUpThirdPartyLoggers();
    }

    private void setUpLogger(Configuration configuration) {
        if (configuration.shouldLogStatus())
            jsUnitLogger = new SystemOutJsUnitLogger();
        else
            jsUnitLogger = new NoOpJsUnitLogger();
    }

    private void setUpLogsDirectory(Configuration configuration) {
        if (!configuration.getLogsDirectory().exists())
            configuration.getLogsDirectory().mkdir();
    }

    protected void ensureConfigurationIsValid() {
        if (!configuration.isValidFor(serverType())) {
            ConfigurationProperty property = serverType().getPropertiesInvalidFor(configuration).get(0);
            throw new ConfigurationException(property, property.getValueString(configuration));
        }
    }

    public abstract ServerType serverType();

    public boolean isFarmServer() {
        return serverType().isFarm();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    protected void setUpThirdPartyLoggers() {
        CommonLogHandler handler = new CommonLogHandler(jsUnitLogger);
        handler.addThirdPartyLogger(Logger.getLogger("org.mortbay"));
        handler.addThirdPartyLogger(Logger.getLogger("com.opensymphony.webwork"));
    }

    public void start() throws Exception {
        setUpHttpServer();
        logStatus(
            "Starting " + (isTemporary() ? "temporary " : "") + "server with configuration:\r\n"
            + XmlUtility.asPrettyString(configuration.asXml(serverType())));
        server.start();
        if (!isTemporary()) {
            VersionChecker checker = VersionChecker.forDefault();
            if (!checker.isUpToDate())
                jsUnitLogger.log(checker.outOfDateString(), false);
        }
    }

    protected boolean isTemporary() {
        return false;
    }

    private void setUpHttpServer() throws Exception {
        server = new HttpServer();
        SocketListener listener = new SocketListener();
        listener.setPort(configuration.getPort());
        server.addListener(listener);

        ServletHttpContext servletContext = new ServletHttpContext();
        servletContext.setContextPath("jsunit");
        servletContext.setResourceBase(configuration.getResourceBase().toString());

        servletContext.addServlet("JSP","*.jsp", JspServlet.class.getName());
        servletContext.addHandler(new ResourceHandler());

        ConfigurationManager.addConfigurationProvider(new XmlConfigurationProvider(xworkXmlName()));

        for (String servletName : servletNames())
            addWebworkServlet(servletContext, servletName);
        server.addContext(servletContext);

        if (Monitor.activeCount() == 0)
            Monitor.monitor();
    }

    protected abstract String xworkXmlName();

    protected abstract List<String> servletNames();

    private void addWebworkServlet(ServletHttpContext servletContext, String name) throws Exception {
        servletContext.addServlet(
            "webwork",
            "/" + name,
            ServletDispatcher.class.getName()
        );
    }

    public void logStatus(String message) {
        jsUnitLogger.log(message, true);
    }

    public Element asXml() {
        return configuration.asXml(serverType());
    }

    public void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    public void dispose() {
        logStatus("Stopping server");
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

    public JsUnitLogger getLogger() {
        return jsUnitLogger;
    }

}