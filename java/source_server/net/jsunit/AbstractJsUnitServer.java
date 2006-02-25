package net.jsunit;

import com.opensymphony.webwork.dispatcher.ServletDispatcher;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationException;
import net.jsunit.configuration.ConfigurationProperty;
import net.jsunit.configuration.ConfigurationType;
import net.jsunit.logging.NoOpStatusLogger;
import net.jsunit.logging.StatusLogger;
import net.jsunit.logging.SystemOutStatusLogger;
import net.jsunit.utility.XmlUtility;
import org.jdom.Element;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.start.Monitor;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public abstract class AbstractJsUnitServer implements XmlRenderable {

    private HttpServer server;
    private StatusLogger statusLogger;
    protected Configuration configuration;

    protected AbstractJsUnitServer(Configuration configuration) {
        this.configuration = configuration;
        ensureConfigurationIsValid();
        if (!configuration.getLogsDirectory().exists())
            configuration.getLogsDirectory().mkdir();
        if (configuration.shouldLogStatus())
            statusLogger = new SystemOutStatusLogger();
        else
            statusLogger = new NoOpStatusLogger();
        setSystemError();
    }

    protected void ensureConfigurationIsValid() {
    	if (!configuration.isValidFor(serverType())) {
            ConfigurationProperty property = serverType().getPropertiesInvalidFor(configuration).get(0);
    		throw new ConfigurationException(property, property.getValueString(configuration));
    	}
    }

    protected abstract ConfigurationType serverType();

    class CommonLogHandler extends Handler {
        public void publish(LogRecord record) {
            statusLogger.log(record.getLevel().toString() + " " + record.getMessage());
        }

        public void flush() {
            System.out.flush();
        }

        public void close() throws SecurityException {
        }
    }

    protected void setSystemError() {
        CommonLogHandler handler = new CommonLogHandler();
        Logger jettyLogger = Logger.getLogger("org.mortbay");
        jettyLogger.addHandler(handler);
        jettyLogger.setUseParentHandlers(false);

        Logger webworkLogger = Logger.getLogger("com.opensymphony.webwork");
        webworkLogger.addHandler(handler);
        webworkLogger.setUseParentHandlers(false);
    }

    public void start() throws Exception {
        setUpHttpServer();
        server.start();
        logStatus("Starting server with configuration:\r\n" + XmlUtility.asPrettyString(configuration.asXml(serverType())));
    }

    private void setUpHttpServer() throws Exception {
        server = new HttpServer();
        SocketListener listener = new SocketListener();
        listener.setPort(configuration.getPort());
        server.addListener(listener);

        ServletHttpContext servletContext = new ServletHttpContext();
        servletContext.setContextPath("jsunit");
        servletContext.setResourceBase(configuration.getResourceBase().toString());
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
        statusLogger.log(message);
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

    public StatusLogger getStatusLogger() {
        return statusLogger;
    }
}
