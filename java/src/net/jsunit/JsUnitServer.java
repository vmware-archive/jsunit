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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.net.URL;

import com.sun.corba.se.internal.iiop.BufferManagerWriteCollect;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class JsUnitServer {
    private static JsUnitServer instance;

    private List results = new ArrayList();
    private HttpServer httpServer;

    private int port;
    private File resourceBase;
    private File logsDirectory;
    private List remoteMachineURLs;
    private List localBrowserFileNames;
    private URL testURL;

    public static void main(String args[]) throws Exception {
        JsUnitServer server = new JsUnitServer();
        if (args.length == 0) {
            new PropertiesConfiguration().configure(server);
        } else
            new ArgumentsConfiguration(Arrays.asList(args)).configure(server);
        server.start();
    }

    public void start() throws Exception {
        Utility.log(toString(), false);
        setUpHttpServer();
        httpServer.start();
    }

    private void setUpHttpServer() throws IOException {
        httpServer = new HttpServer();
        httpServer.addListener(":" + port);
        HttpContext context = httpServer.getContext("/jsunit");
        ServletHandler handler = new ServletHandler();
        handler.addServlet("JsUnitResultAcceptor", "/server", ResultAcceptorServlet.class.getName());
        handler.addServlet("JsUnitResultDisplayer", "/displayer", ResultDisplayerServlet.class.getName());
        handler.addServlet("JsUnitTestRunner", "/runner", TestRunnerServlet.class.getName());
        context.addHandler(handler);
        context.setResourceBase(resourceBase.toString());
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

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("port").append(": ").append(port).append("\n");
        result.append("resourceBase").append(": ").append(resourceBase.getAbsolutePath()).append("\n");
        result.append("logsDirectory").append(": ").append(logsDirectory.getAbsolutePath());
        return result.toString();
    }

    public List getRemoteMachineURLs() {
        return remoteMachineURLs;
    }

    public void setResourceBase(File resourceBase) {
        this.resourceBase = resourceBase;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setLogsDirectory(File logsDirectory) {
        this.logsDirectory = logsDirectory;
    }

    public void setRemoteMachineNames(List names) {
        this.remoteMachineURLs = names;
    }

    public List getLocalBrowserFileNames() {
        return localBrowserFileNames;
    }

    public void setLocalBrowserFileNames(List names) {
        this.localBrowserFileNames = names;
    }

    public void setTestURL(URL url) {
        this.testURL = url;
    }

    public URL getTestURL() {
        return testURL;
    }

    public File getLogsDirectory() {
        return logsDirectory;
    }

    public int getPort() {
        return port;
    }

    public File getResourceBase() {
        return resourceBase;
    }

}