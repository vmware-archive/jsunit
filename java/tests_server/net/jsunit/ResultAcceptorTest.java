package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.interceptor.BrowserResultInterceptor;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultWriter;
import net.jsunit.model.BrowserSource;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ResultAcceptorTest extends TestCase {

    protected Map<String, String[]> requestMap;
    private JsUnitStandardServer server;
    private MockBrowserResultRepository mockBrowserResultRepository;
    private Browser browser;

    public void setUp() throws Exception {
        super.setUp();
        Configuration configuration = new Configuration(new StubConfigurationSource() {

            public String browserFileNames() {
                return "browser1.exe,browser2.exe,browser3.exe";
            }

            public String logStatus() {
                return String.valueOf(Boolean.FALSE);
            }

            public String url() {
                return "http://bar";
            }

        });
        mockBrowserResultRepository = new MockBrowserResultRepository();
        server = new JsUnitStandardServer(configuration, mockBrowserResultRepository, false);
        server.setProcessStarter(new MockProcessStarter());
        browser = new Browser("browser.exe", 1);
        requestMap = new HashMap<String, String[]>();
        requestMap.put(BrowserResultWriter.ID, new String[]{"ID_foo"});
        requestMap.put(BrowserResultWriter.USER_AGENT, new String[]{"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"});
        requestMap.put(BrowserResultWriter.TIME, new String[]{"4.3"});
        requestMap.put(BrowserResultWriter.JSUNIT_VERSION, new String[]{"2.5"});
        requestMap.put(BrowserResultWriter.TEST_CASE_RESULTS, dummyTestCaseStrings());
        requestMap.put("browserId", new String[]{String.valueOf(browser.getId())});
    }

    protected String[] dummyTestCaseStrings() {
        return new String[]{"file:///dummy/path/dummyPage.html:testFoo|1.3|S||", "file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|", "file:///dummy/path/dummyPage.html:testFoo|1.3|F|Test Failure Message|"};
    }

    protected void submit() {
        HttpServletRequest request = new DummyHttpRequest(requestMap);
        server.accept(new BrowserResultInterceptor().build(request, new BrowserSource() {
            public Browser getBrowserById(int id) {
                if (id == browser.getId())
                    return browser;
                fail("shouldn't be asking");
                return null;
            }
        }));
    }

    public void testSubmitResults() {
        launchBrowserTestRun();
        assertNull(server.lastResult());
        submit();
        BrowserResult browserResult1 = server.lastResult();
        assertNotNull(browserResult1);

        launchBrowserTestRun();
        submit();
        BrowserResult browserResult2 = server.lastResult();
        assertNotNull(browserResult2);

        assertNotSame(browserResult1, browserResult2);
    }

    public void testSubmittedResultHeaders() {
        launchBrowserTestRun();
        submit();
        BrowserResult result = server.lastResult();
        assertEquals("ID_foo", result.getId());
        assertEquals("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)", result.getUserAgent());
        assertEquals("2.5", result.getJsUnitVersion());
        assertEquals(1, result.getErrorCount());
        assertEquals(1, result.getFailureCount());
        assertEquals(3, result.getTestCount());
        assertEquals(4.3d, result.getTime(), .001d);
    }

    public void testSubmittedTestCaseResults() {
        launchBrowserTestRun();
        submit();
        BrowserResult result = server.lastResult();
        assertEquals(3, result.getTestCaseResults().size());
    }

    private void launchBrowserTestRun() {
        server.launchBrowserTestRun(new BrowserLaunchSpecification(browser));
    }

    public void testIsWaitingForBrowser() throws InterruptedException {
        Browser someOtherBrowser = new Browser("anotherBrowser.exe", 7);
        launchBrowserTestRun();
        server.launchBrowserTestRun(new BrowserLaunchSpecification(someOtherBrowser));

        assertTrue(server.isWaitingForBrowser(browser));
        assertTrue(server.isWaitingForBrowser(someOtherBrowser));

        submit();
        assertFalse(server.isWaitingForBrowser(browser));
        assertTrue(server.isWaitingForBrowser(someOtherBrowser));
    }

    public void testFindResultById() throws InvalidBrowserIdException {
        assertNull(server.findResultWithId("ID_foo", 1));
        assertEquals("ID_foo", mockBrowserResultRepository.requestedId);
        assertEquals(1, mockBrowserResultRepository.requestedBrowser.getId());
    }

    public void testLog() {
        launchBrowserTestRun();
        submit();
        assertEquals("ID_foo", mockBrowserResultRepository.storedResult.getId());
    }

}