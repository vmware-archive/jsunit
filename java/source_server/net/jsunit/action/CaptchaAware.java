package net.jsunit.action;

public interface CaptchaAware  {
    boolean isProtectedByCaptcha();

    String getCaptchaKey();

    String getAttemptedCaptchaAnswer();

    String getSecretKey();
}
