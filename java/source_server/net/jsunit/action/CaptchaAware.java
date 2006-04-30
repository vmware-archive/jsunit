package net.jsunit.action;

public interface CaptchaAware extends RequestSourceAware {
    boolean isProtectedByCaptcha();

    String getCaptchaKey();

    String getAttemptedCaptchaAnswer();

    String getSecretKey();
}
