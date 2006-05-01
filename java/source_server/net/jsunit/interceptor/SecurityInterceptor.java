package net.jsunit.interceptor;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import net.jsunit.action.CaptchaAware;
import net.jsunit.captcha.CaptchaSpec;
import net.jsunit.captcha.CaptchaValidity;

public class SecurityInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        CaptchaAware captchaAware = (CaptchaAware) invocation.getAction();
        if (captchaAware.isProtectedByCaptcha() && !captchaAware.isIpAddressesTrusted()) {
            String key = captchaAware.getCaptchaKey();
            CaptchaSpec spec = CaptchaSpec.fromEncryptedKey(captchaAware.getSecretKey(), key);
            if (!spec.isValid())
                return "captcha_" + spec.getValidity().name().toLowerCase();
            String answer = captchaAware.getAttemptedCaptchaAnswer();
            return spec.getAnswer().equals(answer) ?
                    invocation.invoke() :
                    "captcha_" + CaptchaValidity.INVALID.name().toLowerCase();
        } else
            return invocation.invoke();
    }

}
