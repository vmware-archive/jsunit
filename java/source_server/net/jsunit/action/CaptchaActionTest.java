package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.JsUnitServerStub;
import net.jsunit.captcha.AesCipher;

import java.io.ByteArrayInputStream;

public class CaptchaActionTest extends TestCase {
    public static final String DUMMY_SECRET_KEY = "1234567890123456";

    public void testSimple() throws Exception {
        CaptchaImageAction action = new CaptchaImageAction();
        action.setJsUnitServer(new JsUnitServerStub() {
            public String getSecretKey() {
                return DUMMY_SECRET_KEY;
            }
        });
        action.setAnswer(new AesCipher(DUMMY_SECRET_KEY).encrypt("theAnswer"));
        action.execute();
        ByteArrayInputStream imageStream = (ByteArrayInputStream) action.getImageStream();
        assertTrue(imageStream.toString().length() > 0);
    }
}
