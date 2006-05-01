package net.jsunit.captcha;

import junit.framework.TestCase;

public class CaptchaSpecTest extends TestCase {
    private static final String SECRET_KEY = "1234567890123456";

    public void testFromAnswerAndTime() throws Exception {
        CaptchaSpec spec = CaptchaSpec.fromAnswerAndTime(SECRET_KEY, "foobar", 12345678);
        assertEquals("12345678_foobar", new AesCipher(SECRET_KEY).decrypt(spec.getEncryptedKey()));
        assertEquals("foobar", spec.getAnswer());
        assertEquals(12345678, spec.getTime());
    }

    public void testFromEncryptedKey() throws Exception {
        String encryptedKey = new AesCipher(SECRET_KEY).encrypt("87654321_barfoo");
        CaptchaSpec spec = CaptchaSpec.fromEncryptedKey(SECRET_KEY, encryptedKey);
        assertEquals(encryptedKey, spec.getEncryptedKey());
        assertEquals("barfoo", spec.getAnswer());
        assertEquals(87654321, spec.getTime());
    }

    public void testFiveMinutesAgo() throws Exception {
        long time = System.currentTimeMillis() - 5 * 60 * 1000;
        String answer = "foobar";
        CaptchaSpec spec = CaptchaSpec.fromAnswerAndTime(SECRET_KEY, answer, time);
        assertEquals(CaptchaValidity.VALID, spec.getValidity());
        assertTrue(spec.isValid());
    }

    public void testOutdated() throws Exception {
        long time = System.currentTimeMillis() - 31 * 60 * 1000;
        String answer = "foobar";
        CaptchaSpec spec = CaptchaSpec.fromAnswerAndTime(SECRET_KEY, answer, time);
        assertEquals(CaptchaValidity.OUTDATED, spec.getValidity());
        assertFalse(spec.isValid());
    }

    public void testBadKey() throws Exception {
        CaptchaSpec spec = CaptchaSpec.fromEncryptedKey(SECRET_KEY, "bad key");
        assertEquals(CaptchaValidity.INVALID, spec.getValidity());
    }

}
