package net.jsunit.action;

import net.jsunit.model.SecurityViolation;

public class SecurityViolationMessage {
    private String ipAddress;
    private SecurityViolation violation;
    private String captchaKey;
    private String attemptedCaptchaAnswer;

    public SecurityViolationMessage(String remoteIpAddress, SecurityViolation violation, String captchaKey, String captchaAnswer) {
        ipAddress = remoteIpAddress;
        this.violation = violation;
        this.captchaKey = captchaKey;
        this.attemptedCaptchaAnswer = captchaAnswer;
    }

    public String generateMessage() {
        return "Security violation from IP address " + ipAddress + ": " + violation.name() + " (key=" + captchaKey + ", answer=" + attemptedCaptchaAnswer + ")";

    }
}
