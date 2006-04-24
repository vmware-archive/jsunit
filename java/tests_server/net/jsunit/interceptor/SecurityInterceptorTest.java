package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.action.RequestSourceAware;

public class SecurityInterceptorTest extends TestCase {
    private SecurityInterceptor interceptor;

    protected void setUp() throws Exception {
        super.setUp();
        interceptor = new SecurityInterceptor();
    }

    public void testOK() throws Exception {
        MockActionInvocation mockInvocation = new MockActionInvocation(new OKSourceAction());
        assertEquals(Action.SUCCESS, interceptor.intercept(mockInvocation));
        assertTrue(mockInvocation.wasInvokeCalled);
    }

    public void testNotOK() throws Exception {
        MockActionInvocation mockInvocation = new MockActionInvocation(new BadSourceAction());
        assertEquals(SecurityInterceptor.DENIED, interceptor.intercept(mockInvocation));
        assertFalse(mockInvocation.wasInvokeCalled);
    }

    static class OKSourceAction implements RequestSourceAware, Action {
        public void setRequestIPAddress(String ipAddress) {
        }

        public void setRequestHost(String host) {
        }

        public String getRequestIpAddress() {
            return "192.168.1.103";
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
