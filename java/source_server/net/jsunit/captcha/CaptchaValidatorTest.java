package net.jsunit.captcha;

import junit.framework.TestCase;

public class CaptchaValidatorTest extends TestCase {
    private static final String DUMMY_SECRET_KEY = "1234567890123456";

    private CaptchaGenerator generator;
    private CaptchaValidator validator;

    protected void setUp() throws Exception {
        super.setUp();
        generator = new CaptchaGenerator(DUMMY_SECRET_KEY);
        validator = new CaptchaValidator(DUMMY_SECRET_KEY);
    }

    public void testGenerateKey() throws Exception {
        String key = generator.generateKey(12345678, "foobar");
        assertEquals("12345678_foobar", new AesCipher("1234567890123456").decrypt(key));
    }

    public void testFiveMinutesAgo() throws Exception {
        long time = System.currentTimeMillis() - 5 * 60* 1000;
        String answer = generator.generateRandomAnswer();
        String key = generator.generateKey(time, answer);
        assertEquals(CaptchaValidity.VALID, validator.determineValidity(key, answer));
        assertEquals(CaptchaValidity.INVALID, validator.determineValidity(key, "invalid answer"));
    }

    public void testOverHalfAnHourAgo() throws Exception {
        long time = System.currentTimeMillis() - 31 * 60* 1000;
        String answer = generator.generateRandomAnswer();
        String key = generator.generateKey(time, answer);
        assertEquals(CaptchaValidity.OUTDATED, validator.determineValidity(key, "invalid answer"));
    }

    public void testBadKey() throws Exception {
        assertEquals(CaptchaValidity.INVALID, validator.determineValidity("not a valid key", "answer"));
    }

}
