package net.jsunit;

import net.jsunit.configuration.ServerConfiguration;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHttpContext;

import java.util.logging.Logger;

/**
 * A simple server that depends only on Jetty (not WebWork) and only serves static files.
 */
public class SimpleWebServer implements WebServer {
    private Server httpServer;
    private ServerConfiguration configuration;
    private Logger logger = Logger.getLogger("net.jsunit");

    public SimpleWebServer(ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean isAlive() {
        return httpServer != null && httpServer.isStarted();
    }

    public void start() throws Exception {  
        if (httpServer == null) {
            String contextPath = "jsunit";
            String resourceBase = configuration.getResourceBase().toString();
            int port = configuration.getPort();

            logger.info("Creating JsUnit simple server" +
                    " on port <" + port + ">" +
                    " with contextPath <" + contextPath + ">" +
                    " and resourceBase <" + resourceBase + ">");

            httpServer = new Server();
            ServletHttpContext jsunitContext = new ServletHttpContext();
            jsunitContext.setContextPath(contextPath);
            jsunitContext.setResourceBase(resourceBase);

            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirAllowed(false);

            jsunitContext.addHandler(resourceHandler);
            httpServer.addContext(jsunitContext);

            SocketListener listener = new SocketListener();
            listener.setPort(port);
            httpServer.addListener(listener);
        }

        logger.info("Starting JsUnit simple server.");
        httpServer.start();
    }

    public void dispose() {
        if (httpServer != null) {
            try {
                logger.info("Stopping JsUnit simple server.");
                httpServer.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
