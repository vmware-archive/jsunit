package net.jsunit.action;

import net.jsunit.model.SecurityViolation;

public interface CaptchaAware {
    boolean isProtectedByCaptcha();

    String getCaptchaKey();

    String getAttemptedCaptchaAnswer();

    String getSecretKey();

    void setSecurityViolation(SecurityViolation violation);

}
