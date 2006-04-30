package net.jsunit.captcha;

public enum CaptchaValidity {
    VALID,
    INVALID,
    OUTDATED;

    public boolean isValid() {
        return this == VALID;
    }
}
