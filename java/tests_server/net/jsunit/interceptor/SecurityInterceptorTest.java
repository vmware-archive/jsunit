package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.action.CaptchaAware;
import net.jsunit.captcha.AesCipher;

public class SecurityInterceptorTest extends TestCase {
    private SecurityInterceptor interceptor;
    private MockCaptchaAware action;
    private MockActionInvocation mockInvocation;
    private static final String SECRET_KEY = "1234567890123456";

    protected void setUp() throws Exception {
        super.setUp();
        interceptor = new SecurityInterceptor();
        action = new MockCaptchaAware();
        mockInvocation = new MockActionInvocation(action);
    }

    public void testProtectedValid() throws Exception {
        action.isProtected = true;
        action.key = new AesCipher(SECRET_KEY).encrypt(System.currentTimeMillis() + "_theCorrectAnswer");
        action.answer = "theCorrectAnswer";
        assertEquals(Action.SUCCESS, interceptor.intercept(mockInvocation));
        assertTrue(mockInvocation.wasInvokeCalled);
    }

    public void testProtectedInvalid() throws Exception {
        action.isProtected = true;
        action.key = new AesCipher(SECRET_KEY).encrypt(System.currentTimeMillis() + "_theCorrectAnswer");
        action.answer = "bad answer";
        assertEquals("captcha_invalid", interceptor.intercept(mockInvocation));
        assertFalse(mockInvocation.wasInvokeCalled);
    }

    public void testProtectedOutdated() throws Exception {
        action.isProtected = true;
        action.key = new AesCipher(SECRET_KEY).encrypt("0_theCorrectAnswer");
        action.answer = "theCorrectAnswer";
        assertEquals("captcha_outdated", interceptor.intercept(mockInvocation));
        assertFalse(mockInvocation.wasInvokeCalled);
    }

    public void testUnprotected() throws Exception {
        action.isProtected = false;
        assertEquals(Action.SUCCESS, interceptor.intercept(mockInvocation));
        assertTrue(mockInvocation.wasInvokeCalled);
    }

    public void testProtectedBadKey() throws Exception {
        action.isProtected = true;
        action.key = "bad key";
        action.answer = "bad answer";
        assertEquals("captcha_invalid", interceptor.intercept(mockInvocation));
        assertFalse(mockInvocation.wasInvokeCalled);
    }

    public void testUnprotectedBadKey() throws Exception {
        action.isProtected = false;
        action.key = "bad key";
        action.answer = "bad answer";
        assertEquals(Action.SUCCESS, interceptor.intercept(mockInvocation));
        assertTrue(mockInvocation.wasInvokeCalled);
    }

    static class MockCaptchaAware implements CaptchaAware, Action {
        private boolean isProtected;
        private String key;
        private String answer;

        public String execute() throws Exception {
            return SUCCESS;
        }

        public boolean isProtectedByCaptcha() {
            return isProtected;
        }

        public String getCaptchaKey() {
            return key;
        }

        public String getAttemptedCaptchaAnswer() {
            return answer;
        }

        public String getSecretKey() {
            return SECRET_KEY;
        }

        public void setRequestIPAddress(String ipAddress) {
        }

        public void setRequestHost(String host) {
        }

        public void setReferrer(String referrer) {
        }
    }

}
