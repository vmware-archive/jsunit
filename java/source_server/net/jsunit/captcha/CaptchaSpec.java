package net.jsunit.captcha;

public class CaptchaSpec {
    private AesCipher cipher;
    private String encryptedKey;
    private String answer;
    private long time;
    private CaptchaValidity validity;

    public CaptchaSpec(String secretKey) {
        cipher = new AesCipher(secretKey);
    }

    private CaptchaSpec(String secretKey, String encrypted) {
        this(secretKey);
        this.encryptedKey = encrypted;
        parseEncryptedKey();
    }

    public CaptchaSpec(String secretKey, String answer, long time) {
        this(secretKey);
        this.answer = answer;
        this.time = time;
        this.encryptedKey = cipher.encrypt(time + "_" + answer);
        determineValidity();
    }

    private void parseEncryptedKey() {
        String decryptedKey = null;
        try {
            decryptedKey = cipher.decrypt(encryptedKey);
            String[] strings = decryptedKey.split("_");
            time = Long.parseLong(strings[0]);
            answer = strings[1];
            determineValidity();
        } catch (Throwable t) {
            this.validity = CaptchaValidity.INVALID;
        }
    }

    private void determineValidity() {
        long earliestAllowed = halfAnHourAgo();
        if (time < earliestAllowed)
            this.validity = CaptchaValidity.OUTDATED;
        else
            this.validity = CaptchaValidity.VALID;
    }

    public boolean isValid() {
        return validity.isValid();
    }

    public String getAnswer() {
        return answer;
    }

    private long halfAnHourAgo() {
        return System.currentTimeMillis() - 30 * 60 * 1000;
    }

    public static CaptchaSpec fromEncryptedKey(String secretKey, String encryptedKey) {
        return new CaptchaSpec(secretKey, encryptedKey);
    }

    public static CaptchaSpec fromAnswerAndTime(String secretKey, String answer, long time) {
        return new CaptchaSpec(secretKey, answer, time);
    }

    public static CaptchaSpec create(String secretKey) {
        return fromAnswerAndTime(secretKey, new CaptchaGenerator().generateRandomAnswer(), System.currentTimeMillis());
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public CaptchaValidity getValidity() {
        return validity;
    }

    public long getTime() {
        return time;
    }

}
