package net.jsunit.interceptor;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import net.jsunit.action.CaptchaAware;
import net.jsunit.captcha.CaptchaSpec;
import net.jsunit.captcha.SecurityViolation;

public class SecurityInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        CaptchaAware captchaAware = (CaptchaAware) invocation.getAction();
        if (captchaAware.isProtectedByCaptcha()) {
            String key = captchaAware.getCaptchaKey();
            CaptchaSpec spec = CaptchaSpec.fromEncryptedKey(captchaAware.getSecretKey(), key);
            SecurityViolation violation = null;
            if (!spec.isValid())
                violation = spec.getValidity().getSecurityViolation();
            else if (!spec.getAnswer().equals(captchaAware.getAttemptedCaptchaAnswer()))
                violation = SecurityViolation.FAILED_CAPTCHA;
            if (violation != null)
                captchaAware.setSecurityViolation(violation);
        }
        return invocation.invoke();
    }

}
