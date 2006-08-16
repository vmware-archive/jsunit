package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.DummyHttpRequest;
import net.jsunit.action.BrowserSelectionAware;
import net.jsunit.model.Browser;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowserSelectionInterceptorTest extends TestCase {
    private BrowserSelectionInterceptor interceptor;
    private MockAction action;
    private MockActionInvocation invocation;

    protected void setUp() throws Exception {
        super.setUp();
        interceptor = new BrowserSelectionInterceptor();
        action = new MockAction();
        invocation = new MockActionInvocation(action);
    }

    public void testValidSelection() throws Exception {
        setUpRequestWithBrowserIds("0", "2");
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        assertTrue(invocation.wasInvokeCalled);
        assertNull(action.errorMessage);
        assertEquals(Arrays.asList(action.browser0, action.browser2), action.selectedBrowsers);
    }

    public void testDuplicate() throws Exception {
        setUpRequestWithBrowserIds("0", "2", "2");
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        assertTrue(invocation.wasInvokeCalled);
        assertNull(action.errorMessage);
        assertEquals(Arrays.asList(action.browser0, action.browser2), action.selectedBrowsers);
    }

    public void testInvalidId() throws Exception {
        setUpRequestWithBrowserIds("0", "56");
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        assertTrue(invocation.wasInvokeCalled);
        assertEquals("Invalid browser ID: 56", action.errorMessage);
    }

    public void testNonIntegerId() throws Exception {
        setUpRequestWithBrowserIds("1", "foobar", "0");
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        assertTrue(invocation.wasInvokeCalled);
        assertEquals("Invalid browser ID: foobar", action.errorMessage);
    }

    public void testNoSelection() throws Exception {
        setUpRequestWithBrowserIds();
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        assertTrue(invocation.wasInvokeCalled);
        assertNull(action.errorMessage);
        assertEquals(action.getAllBrowsers(), action.selectedBrowsers);
    }

    private void setUpRequestWithBrowserIds(String... browserIds) {
        Map<String, String[]> requestMap = new HashMap<String, String[]>();
        HttpServletRequest request = new DummyHttpRequest(requestMap);
        String[] values = new String[browserIds.length];
        System.arraycopy(browserIds, 0, values, 0, browserIds.length);
        requestMap.put("browserId", values);
        ServletActionContext.setRequest(request);
    }

    static class MockAction implements Action, BrowserSelectionAware {
        private Browser browser0 = new Browser("browser0.exe", 0);
        private Browser browser1 = new Browser("browser1.exe", 1);
        private Browser browser2 = new Browser("browser2.exe", 2);
        private List<Browser> selectedBrowsers;
        private String errorMessage;

        public String execute() throws Exception {
            return SUCCESS;
        }

        public void setSelectedBrowsers(List<Browser> browsers) {
            this.selectedBrowsers = browsers;
        }

        public Browser getBrowserById(int id) {
            for (Browser browser : getAllBrowsers()) {
                if (browser.getId() == id)
                    return browser;
            }
            return null;
        }

        public List<Browser> getAllBrowsers() {
            return Arrays.asList(new Browser[]{browser0, browser1, browser2});
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
