package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.action.ReferrerAware;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;

public class SecurityInterceptorTest extends TestCase {
    private SecurityInterceptor interceptor;
    private ReferrerAction action;
    private MockActionInvocation mockInvocation;

    protected void setUp() throws Exception {
        super.setUp();
        interceptor = new SecurityInterceptor();
        action = new ReferrerAction();
        mockInvocation = new MockActionInvocation(action);
    }

    public void testNoReferrer() throws Exception {
        action.referrer = null;
        action.restrict = "http://www.jsunit.net";
        assertEquals(SecurityInterceptor.DENIED, interceptor.intercept(mockInvocation));
        assertFalse(mockInvocation.wasInvokeCalled);
    }

    public void testNoRestrict() throws Exception {
        action.referrer = "http://www.jsunit.net/myPage.html";
        action.restrict = null;
        assertEquals(Action.SUCCESS, interceptor.intercept(mockInvocation));
        assertTrue(mockInvocation.wasInvokeCalled);
    }

    public void testReferrerMatchesRestrict() throws Exception {
        action.referrer = "http://www.jsunit.net/myPage.html";
        action.restrict = "http://www.jsunit.net";
        assertEquals(Action.SUCCESS, interceptor.intercept(mockInvocation));
        assertTrue(mockInvocation.wasInvokeCalled);
    }

    public void testReferrerDoesNotMatchRestrict() throws Exception {
        action.referrer = "http://www.hacker.com/hackerPage.html";
        action.restrict = "http://www.jsunit.net";
        assertEquals(SecurityInterceptor.DENIED, interceptor.intercept(mockInvocation));
        assertFalse(mockInvocation.wasInvokeCalled);
    }

    static class ReferrerAction implements ReferrerAware, Action {
        private String referrer;
        private String restrict;

        public String execute() throws Exception {
            return SUCCESS;
        }

        public void setReferrer(String referrer) {
        }

        public String getReferrer() {
            return referrer;
        }

        public Configuration getConfiguration() {
            return new Configuration(new DummyConfigurationSource() {
                public String runnerReferrerRestrict() {
                    return restrict;
                }
            });
        }
    }

}
