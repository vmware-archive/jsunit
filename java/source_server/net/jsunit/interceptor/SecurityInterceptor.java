package net.jsunit.interceptor;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import net.jsunit.action.CaptchaAware;
import net.jsunit.captcha.CaptchaValidator;
import net.jsunit.captcha.CaptchaValidity;

public class SecurityInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        CaptchaAware captchaAware = (CaptchaAware) invocation.getAction();
        if (captchaAware.isProtectedByCaptcha()) {
            CaptchaValidator validator = new CaptchaValidator(captchaAware.getSecretKey());
            String key = captchaAware.getCaptchaKey();
            String answer = captchaAware.getAttemptedCaptchaAnswer();
            CaptchaValidity captchaValidity = validator.determineValidity(key, answer);
            return captchaValidity.isValid() ? invocation.invoke() : "captcha_" + captchaValidity.name().toLowerCase();
        }
        else
            return invocation.invoke();
    }

}
