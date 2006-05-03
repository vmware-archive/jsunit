package net.jsunit.captcha;

public enum CaptchaValidity {
    VALID(null),
    INVALID(SecurityViolation.FAILED_CAPTCHA),
    OUTDATED(SecurityViolation.OUTDATED_CAPTCHA);
    private SecurityViolation violation;

    CaptchaValidity(SecurityViolation violation) {
        this.violation = violation;
    }

    public boolean isValid() {
        return this == VALID;
    }

    public SecurityViolation getSecurityViolation() {
        return violation;
    }
}
