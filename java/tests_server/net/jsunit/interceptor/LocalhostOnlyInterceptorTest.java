package net.jsunit.interceptor;

import junit.framework.TestCase;
import com.opensymphony.xwork.Action;
import net.jsunit.action.RequestSourceAware;

public class LocalhostOnlyInterceptorTest extends TestCase {

    private LocalhostOnlyInterceptor interceptor;

    protected void setUp() throws Exception {
        super.setUp();
        interceptor = new LocalhostOnlyInterceptor();
    }

    public void testOK() throws Exception {
        MockActionInvocation mockInvocation = new MockActionInvocation(new OKSourceAction());
        assertEquals(Action.SUCCESS, interceptor.intercept(mockInvocation));
        assertTrue(mockInvocation.wasInvokeCalled);
    }

    public void testNotOK() throws Exception {
        MockActionInvocation mockInvocation = new MockActionInvocation(new BadSourceAction());
        assertEquals(LocalhostOnlyInterceptor.DENIED_NOT_LOCALHOST, interceptor.intercept(mockInvocation));
        assertFalse(mockInvocation.wasInvokeCalled);
    }

    static class OKSourceAction implements RequestSourceAware, Action {
        public void setRequestIPAddress(String ipAddress) {
        }

        public void setRequestHost(String host) {
        }

        public String getRequestIpAddress() {
            return "127.0.0.1";
        }

        public String execute() throws Exception {
            return SUCCESS;
        }
    }

    static class BadSourceAction implements RequestSourceAware, Action {
        public void setRequestIPAddress(String ipAddress) {
        }

        public void setRequestHost(String host) {
        }

        public String getRequestIpAddress() {
            return "74.231.9.342";
        }

        public String execute() throws Exception {
            return null;
        }
    }

}
