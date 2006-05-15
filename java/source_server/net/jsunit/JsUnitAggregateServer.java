package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.configuration.ServerType;
import net.jsunit.model.RemoteServerConfigurationSource;
import net.jsunit.model.UserRepository;
import net.jsunit.model.DefaultUserRepository;
import net.jsunit.model.User;
import net.jsunit.utility.FileUtility;
import org.apache.axis.transport.http.AdminServlet;
import org.apache.axis.transport.http.AxisServlet;
import org.apache.jasper.servlet.JspServlet;
import org.mortbay.http.SocketListener;
import org.mortbay.http.SslListener;
import org.mortbay.http.handler.ForwardHandler;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHttpContext;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsUnitAggregateServer extends AbstractJsUnitServer implements RemoteServerConfigurationSource {

    private RemoteServerHitter hitter;
    private List<RemoteConfiguration> cachedRemoteConfigurations;
    private JsUnitAggregateServer.RemoteConfigurationCacheUpdater updater;
    private UserRepository userRepository = new DefaultUserRepository();

    public JsUnitAggregateServer(Configuration configuration) {
        this(configuration, new RemoteMachineServerHitter());
    }

    private void addRootContext() {
        ServletHttpContext rootContext = new ServletHttpContext();
        rootContext.setContextPath("/");
        rootContext.addHandler(new ForwardHandler("/jsunit"));
        server.addContext(rootContext);
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
    }

    protected void postStart() {
        super.postStart();
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

    protected void setUpJsUnitContext(ServletHttpContext jsunitContext) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        super.setUpJsUnitContext(jsunitContext);
        jsunitContext.addWelcomeFile("java/jsp/fragmentRunner.jsp");
        jsunitContext.addServlet("JSP", "*.jsp", JspServlet.class.getName());
    }

    protected void setUpHttpServer() throws Exception {
        super.setUpHttpServer();
        addRootContext();
        ServletHttpContext axisContext = new ServletHttpContext();
        axisContext.setContextPath("/");
        axisContext.setResourceBase(configuration.getResourceBase().toString());
        axisContext.addServlet("AxisAdmin", "/axisadmin", AdminServlet.class.getName());
        axisContext.addServlet("Axis", "/services/*", AxisServlet.class.getName());
        axisContext.setMimeMapping("wsdl", "text/xml");
        axisContext.setMimeMapping("xsd", "text/xml");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        axisContext.addHandler(resourceHandler);
        server.addContext(axisContext);
    }

    public User authenticateUser(String username, String password) {
        return userRepository.find(username, password);
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PlatformType getPlatformType() {
        return PlatformType.resolve();
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

    protected List<String> servletNames() {
        List<String> names = super.servletNames();
        names.add("adminpage");
        names.add("captchaimage");
        names.add("fragmentrunnerpage");
        names.add("helppage");
        names.add("latestversion");
        names.add("logdisplayerpage");
        names.add("myaccountpage");
        names.add("processcreateaccount");
        names.add("uploadrunnerpage");
        names.add("urlrunnerpage");
        return names;
    }

    protected SocketListener createSocketListener() {
        if (FileUtility.doesFileExist("java" + File.separator + "config" + File.separator + "keystore")) {
            SslListener sslListener = new SslListener();
            sslListener.setKeystore("java/config/keystore");
            sslListener.setPassword(System.getProperty(PROPERTY_SSL_PASSWORD));
            sslListener.setKeyPassword(System.getProperty(PROPERTY_SSL_PASSWORD));
            return sslListener;
        } else
            return super.createSocketListener();
    }

}
