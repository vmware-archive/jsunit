package net.jsunit.captcha;

public class CaptchaValidator {
    private AesCipher aesCipher;

    public CaptchaValidator(String secretKey) {
        aesCipher = new AesCipher(secretKey);
    }

    public CaptchaValidity determineValidity(String key, String attemptedAnswer) {
        String decryptedKey = null;
        try {
            decryptedKey = aesCipher.decrypt(key);
        } catch (Exception e) {
            return CaptchaValidity.INVALID;
        }
        String[] strings = decryptedKey.split("_");
        long time = Long.parseLong(strings[0]);
        long earliestAllowed = halfAnHourAgo();
        if (time < earliestAllowed)
            return CaptchaValidity.OUTDATED;
        String correctAnswer = strings[1];
        return attemptedAnswer.equals(correctAnswer) ? CaptchaValidity.VALID : CaptchaValidity.INVALID;
    }

    private long halfAnHourAgo() {
        return System.currentTimeMillis() - 30 * 60 * 1000;
    }

}
