package net.jsunit.action;

import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.JsUnitServerStub;
import net.jsunit.captcha.AesCipher;

import java.io.ByteArrayInputStream;

public class CaptchaActionTest extends TestCase {
    public static final String SECRET_KEY = "1234567890123456";

    private CaptchaImageAction action;

    protected void setUp() throws Exception {
        super.setUp();
        action = new CaptchaImageAction();
        action.setJsUnitServer(new JsUnitServerStub() {
            public String getSecretKey() {
                return SECRET_KEY;
            }
        });
    }

    public void testSimple() throws Exception {
        action.setCaptchaKey(new AesCipher(SECRET_KEY).encrypt(System.currentTimeMillis() + "_theAnswer"));
        assertEquals(Action.SUCCESS, action.execute());
        ByteArrayInputStream imageStream = (ByteArrayInputStream) action.getImageStream();
        assertTrue(imageStream.toString().length() > 0);
    }

    public void testOutdated() throws Exception {
        action.setCaptchaKey(new AesCipher(SECRET_KEY).encrypt("0_theAnswer"));
        assertEquals(Action.ERROR, action.execute());
        assertNull(action.getImageStream());
    }

    public void testInvalid() throws Exception {
        action.setCaptchaKey("not a valid key");
        assertEquals(Action.ERROR, action.execute());
        assertNull(action.getImageStream());
    }
}
