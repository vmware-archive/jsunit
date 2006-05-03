package net.jsunit.captcha;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;

public class CaptchaGeneratorTest extends TestCase {
    private CaptchaGenerator generator;

    protected void setUp() throws Exception {
        super.setUp();
        generator = new CaptchaGenerator();
    }

    public void testRandomAnswer() throws Exception {
        assertEquals(6, generator.generateRandomAnswer().length());
    }

    public void testImage() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        generator.createImage("foobar", stream);
        assertTrue(stream.toByteArray().length > 0);
    }

}
