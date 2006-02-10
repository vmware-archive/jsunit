package net.jsunit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationConstants;
import net.jsunit.configuration.EnvironmentVariablesConfigurationSource;
import net.jsunit.interceptor.BrowserResultInterceptor;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultWriter;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class ResultAcceptorTest extends TestCase {
    protected Map<String, String[]> requestMap;
    private JsUnitServer server;

    public ResultAcceptorTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        System.setProperty(ConfigurationConstants.BROWSER_FILE_NAMES, "foo");
        System.setProperty(ConfigurationConstants.URL, "http://bar");
        server = new JsUnitServer(new Configuration(new EnvironmentVariablesConfigurationSource()));
        requestMap = new HashMap<String, String[]>();
        requestMap.put(BrowserResultWriter.ID, new String[] {"ID_foo"});
        requestMap.put(BrowserResultWriter.USER_AGENT, new String[] {"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"});
        requestMap.put(BrowserResultWriter.TIME, new String[] {"4.3"});
        requestMap.put(BrowserResultWriter.JSUNIT_VERSION, new String[] {"2.5"});
        requestMap.put(BrowserResultWriter.TEST_CASES, dummyTestCaseStrings());
    }

    public void tearDown() throws Exception {
        System.getProperties().remove(ConfigurationConstants.BROWSER_FILE_NAMES);
        System.getProperties().remove(ConfigurationConstants.URL);
        File logFile = BrowserResult.logFileForId(server.getLogsDirectory(), "ID_foo");
        if (logFile.exists())
            logFile.delete();
        super.tearDown();
    }

    protected String[] dummyTestCaseStrings() {
        return new String[]{"file:///dummy/path/dummyPage.html:testFoo|1.3|S||", "file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|", "file:///dummy/path/dummyPage.html:testFoo|1.3|F|Test Failure Message|"};
    }

    protected void submit() {
        HttpServletRequest request = new DummyHttpRequest(requestMap);
        server.accept(BrowserResultInterceptor.resultFromRequest(request));
    }

    public void testSubmitResults() {
        assertEquals(0, server.getResults().size());
        submit();
        assertEquals(1, server.getResults().size());
        submit();
        assertEquals(1, server.getResults().size());
    }

    public void testSubmittedResultHeaders() {
        submit();
        BrowserResult result = server.getResults().get(0);
        assertEquals("ID_foo", result.getId());
        assertEquals("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)", result.getUserAgent());
        assertEquals("2.5", result.getJsUnitVersion());
        assertEquals(1, result.getErrorCount());
        assertEquals(1, result.getFailureCount());
        assertEquals(3, result.getTestCount());
        assertEquals(4.3d, result.getTime(), .001d);
    }

    public void testSubmittedTestCaseResults() {
        submit();
        BrowserResult result = server.getResults().get(0);
        assertEquals(3, result.getTestCaseResults().size());
    }

    public void testHasReceivedResultSinceDate() throws InterruptedException {
        assertFalse(server.hasReceivedResultSince(System.currentTimeMillis()));
        long time = System.currentTimeMillis();
        Thread.sleep(100);
        submit();
        assertTrue(server.hasReceivedResultSince(time));
    }

    public void testFindResultById() {
        assertNull(server.findResultWithId("ID_foo"));
        submit();
        assertNotNull(server.findResultWithId("ID_foo"));
        assertNull(server.findResultWithId("Invalid ID"));
        server.clearResults();
        //should look on disk when not in memory
        assertNotNull(server.findResultWithId("ID_foo"));
        assertNull(server.findResultWithId("Invalid ID"));
    }

    public void testLog() {
        File logFile = BrowserResult.logFileForId(server.getLogsDirectory(), "ID_foo");
        assertFalse(logFile.exists());
        submit();
        assertTrue(logFile.exists());
    }
}