package net.jsunit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.interceptor.BrowserResultInterceptor;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultWriter;

public class ResultAcceptorTest extends TestCase {
    protected Map<String, String[]> requestMap;
    private StandardJsUnitServer server;
    private Configuration configuration;

    public ResultAcceptorTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        configuration = new Configuration(new StubConfigurationSource() {

            public String browserFileNames() {
                return "foo";
            }

            public String logStatus() {
                return String.valueOf(Boolean.FALSE);
            }

            public String url() {
                return "http://bar";
            }

        });
        server = new StandardJsUnitServer(configuration);
        requestMap = new HashMap<String, String[]>();
        requestMap.put(BrowserResultWriter.ID, new String[]{"ID_foo"});
        requestMap.put(BrowserResultWriter.USER_AGENT, new String[]{"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"});
        requestMap.put(BrowserResultWriter.TIME, new String[]{"4.3"});
        requestMap.put(BrowserResultWriter.JSUNIT_VERSION, new String[]{"2.5"});
        requestMap.put(BrowserResultWriter.TEST_CASES, dummyTestCaseStrings());
    }

    protected String[] dummyTestCaseStrings() {
        return new String[]{"file:///dummy/path/dummyPage.html:testFoo|1.3|S||", "file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|", "file:///dummy/path/dummyPage.html:testFoo|1.3|F|Test Failure Message|"};
    }

    protected void submit() {
        HttpServletRequest request = new DummyHttpRequest(requestMap);
        server.accept(new BrowserResultInterceptor().build(request));
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

    public void testFindResultByIdInMemoryOrOnDisk() {
        assertNull(server.findResultWithId("ID_foo"));
        submit();
        assertFalse(server.getResults().isEmpty());
        assertNotNull(server.findResultWithId("ID_foo"));
        assertNull(server.findResultWithId("Invalid ID"));
        server.clearResults();
        assertTrue(server.getResults().isEmpty());
        assertNotNull(server.findResultWithId("ID_foo"));
        assertNull(server.findResultWithId("Invalid ID"));
    }

    public void testLog() {
        File logFile = BrowserResult.logFileForId(configuration.getLogsDirectory(), "ID_foo");
        assertFalse(logFile.exists());
        submit();
        assertTrue(logFile.exists());
    }

    public void tearDown() throws Exception {
        File logFile = BrowserResult.logFileForId(configuration.getLogsDirectory(), "ID_foo");
        if (logFile.exists())
            logFile.delete();
        super.tearDown();
    }
}