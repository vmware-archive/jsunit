package net.jsunit;

import net.jsunit.servlet.ResultAcceptorServlet;
import net.jsunit.servlet.ResultDisplayerServlet;
import net.jsunit.servlet.TestRunnerServlet;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpServer;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.awt.image.renderable.RenderableImage;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class JsUnitServer {
    private static JsUnitProperties properties;
    private static JsUnitServer instance;

    private List results = new ArrayList();
    public static String PROPERTIES_FILE_NAME = "jsunit.properties";
    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_RESOURCE_BASE = ".";
    private HttpServer httpServer;

    private JsUnitServer() {
        resolveJsUnitProperties();
    }

    public static void main(String args[]) throws Exception {
        new JsUnitServer().start();
    }

    public void start() throws Exception {
        Utility.log(properties.toString(), false);
        setUpHttpServer();
        httpServer.start();
    }

    private void setUpHttpServer() throws IOException {
        httpServer.addListener(":" + properties.port());
        HttpContext context = httpServer.getContext("/jsunit");
        ServletHandler handler = new ServletHandler();
        handler.addServlet("JsUnitResultAcceptor", "/acceptor", ResultAcceptorServlet.class.getName());
        handler.addServlet("JsUnitResultDisplayer", "/displayer", ResultDisplayerServlet.class.getName());
        handler.addServlet("JsUnitTestRunner", "/runner", TestRunnerServlet.class.getName());
        context.addHandler(handler);
        context.setResourceBase(properties.resourceBase().toString());
        context.addHandler(new ResourceHandler());
        httpServer.addContext(context);
    }

    public static JsUnitServer instance() {
        if (instance == null)
            instance = new JsUnitServer();
        return instance;
    }

    public TestSuiteResult accept(HttpServletRequest request) {
        TestSuiteResult result = TestSuiteResult.fromRequest(request);
        TestSuiteResult existingResultWithSameId =
                findResultWithId(result.getId());
        if (existingResultWithSameId != null)
            results.remove(existingResultWithSameId);
        results.add(result);
        result.writeLog();
        return result;
    }

    public List getResults() {
        return results;
    }

    public void clearResults() {
        results.clear();
    }

    public TestSuiteResult findResultWithId(String id) {
        TestSuiteResult result = findResultWithIdInResultList(id);
        if (result == null)
            result = TestSuiteResult.findResultWithIdInResultLogs(id);
        return result;
    }

    private TestSuiteResult findResultWithIdInResultList(String id) {
        Iterator it = getResults().iterator();
        while (it.hasNext()) {
            TestSuiteResult result = (TestSuiteResult) it.next();
            if (result.hasId(id))
                return result;
        }
        return null;
    }

    public Properties resolveJsUnitProperties() {
        if (properties == null)
            properties = new JsUnitProperties(PROPERTIES_FILE_NAME);
        return properties;
    }

    public TestSuiteResult lastResult() {
        List results = getResults();
        return results.isEmpty()
                ? null
                : (TestSuiteResult) results.get(results.size() - 1);
    }

    public int resultsCount() {
        return getResults().size();
    }

    public void stop() throws InterruptedException {
        if (httpServer != null) {
            httpServer.stop();
            httpServer = null;
        }

    }

    public JsUnitProperties getJsUnitProperties() {
        return properties;
    }
}