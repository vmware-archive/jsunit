package net.jsunit.action;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.DummyHttpRequest;
import net.jsunit.interceptor.MockActionInvocation;
import net.jsunit.interceptor.RemoteMachineUrlSelectionInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteMachineURLSelectionInterceptorTest extends TestCase {

    private RemoteMachineUrlSelectionInterceptor interceptor;

    private MockAction action;
    private MockActionInvocation invocation;

    protected void setUp() throws Exception {
        super.setUp();
        interceptor = new RemoteMachineUrlSelectionInterceptor();
        action = new MockAction();
        invocation = new MockActionInvocation(action);
    }

    public void testValidSelection() throws Exception {
        setUpRequestWithURLIds("0", "2");
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        assertTrue(invocation.wasInvokeCalled);
        assertNull(action.invalidIdString);
        assertEquals(Arrays.asList(action.url0, action.url2), action.selectedURLs);
    }

    public void testInvalidId() throws Exception {
        setUpRequestWithURLIds("0", "56");
        assertEquals(Action.ERROR, interceptor.intercept(invocation));
        assertFalse(invocation.wasInvokeCalled);
        assertEquals("56", action.invalidIdString);
        assertNull(action.selectedURLs);
    }

    public void testNonIntegerId() throws Exception {
        setUpRequestWithURLIds("1", "foobar", "0");
        assertEquals(Action.ERROR, interceptor.intercept(invocation));
        assertFalse(invocation.wasInvokeCalled);
        assertEquals("foobar", action.invalidIdString);
        assertNull(action.selectedURLs);
    }

    public void testNoSelection() throws Exception {
        setUpRequestWithURLIds();
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        assertTrue(invocation.wasInvokeCalled);
        assertNull(action.invalidIdString);
        assertEquals(action.getAllRemoteMachineURLs(), action.selectedURLs);
    }

    private void setUpRequestWithURLIds(String... urlIds) {
        Map<String, String[]> requestMap = new HashMap<String, String[]>();
        HttpServletRequest request = new DummyHttpRequest(requestMap);
        String[] values = new String[urlIds.length];
        System.arraycopy(urlIds, 0, values, 0, urlIds.length);
        requestMap.put("urlId_browserId", values);
        ServletActionContext.setRequest(request);
    }

    static class MockAction implements Action, RemoteMachineURLSelectionAware {
        private URL url0, url1, url2;
        private String invalidIdString;
        private List<URL> selectedURLs;

        public MockAction() throws MalformedURLException {
            url0 = new URL("http://www.example.com");
            url1 = new URL("http://www.example.net");
            url2 = new URL("http://www.example.org");
        }

        public String execute() throws Exception {
            return SUCCESS;
        }

        public void setSelectedRemoteMachineURLs(List<URL> urls) {
            this.selectedURLs = urls;
        }

        public void setInvalidRemoteMachineURLId(String invalidId) {
            this.invalidIdString = invalidId;
        }

        public URL getRemoteMachineURLById(int id) {
            return getAllRemoteMachineURLs().get(id);
        }

        public List<URL> getAllRemoteMachineURLs() {
            return Arrays.asList(new URL[]{url0, url1, url2});
        }
    }
}
