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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class JsUnitServer {
    private static Properties properties;
    private static JsUnitServer instance;
    private List results = new ArrayList();
    public static String PROPERTY_PORT = "port";
    public static String PROPERTY_RESOURCE_BASE = "resourceBase";
    public static String PROPERTY_LOGS_DIRECTORY = "logsDirectory";
    public static String PROPERTIES_FILE_NAME = "jsunit.properties";
    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_RESOURCE_BASE = ".";
    public static HttpServer httpServer;

    private JsUnitServer() {
    }

    public static void main(String args[]) throws Exception {
        new JsUnitServer().start();
    }

    public void start() throws Exception {
        httpServer = new HttpServer();
        httpServer.addListener(":" + port());
        HttpContext context = httpServer.getContext("/jsunit");
        ServletHandler handler = new ServletHandler();
        handler.addServlet("JsUnitResultAcceptor", "/acceptor", ResultAcceptorServlet.class.getName());
        handler.addServlet("JsUnitResultDisplayer", "/displayer", ResultDisplayerServlet.class.getName());
        handler.addServlet("JsUnitTestRunner", "/runner", TestRunnerServlet.class.getName());
        context.addHandler(handler);
        context.setResourceBase(resourceBase());
        context.addHandler(new ResourceHandler());
        httpServer.addContext(context);
        httpServer.start();
    }

    public void stop() throws Exception {
        if (httpServer != null) {
            httpServer.stop();
            httpServer = null;
        }
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

    public Properties propertiesFromFileName(String fileName) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(new FileInputStream(fileName));
            } catch (Exception e) {
                throw new RuntimeException("Could not load " + fileName);
            }
        }
        return properties;
    }

    public Properties jsUnitProperties() {
        return propertiesFromFileName(PROPERTIES_FILE_NAME);
    }

    public String resourceBase() {
        String result = jsUnitProperties().getProperty(PROPERTY_RESOURCE_BASE);
        if (Utility.isEmpty(result))
            result = JsUnitServer.DEFAULT_RESOURCE_BASE;
        return result;
    }

    public String logsDirectory() {
        String result = jsUnitProperties().getProperty(PROPERTY_LOGS_DIRECTORY);
        if (Utility.isEmpty(result))
            result =
                    resourceBase()
                    + File.separator
                    + "java"
                    + File.separator
                    + "logs";
        return result;
    }

    public int port() {
        int result;
        String portString = jsUnitProperties().getProperty(PROPERTY_PORT);
        if (Utility.isEmpty(portString))
            result = DEFAULT_PORT;
        else
            result = Integer.parseInt(portString);
        return result;
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
}
