package net.jsunit.action;

import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.JsUnitServerStub;
import net.jsunit.captcha.AesCipher;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;

public class CaptchaActionTest extends TestCase {
    public static final String SECRET_KEY = "1234567890123456";

    private CaptchaImageAction action;

    protected void setUp() throws Exception {
        super.setUp();
        action = new CaptchaImageAction();
        action.setJsUnitServer(new JsUnitServerStub() {
            public Configuration getConfiguration() {
                return new Configuration(new DummyConfigurationSource()) {
                    public String getSecretKey() {
                        return SECRET_KEY;
                    }
                };
            }
        });
    }

    public void testValid() throws Exception {
        action.setCaptchaKey(new AesCipher(SECRET_KEY).encrypt(System.currentTimeMillis() + "_theAnswer"));
        assertEquals(Action.SUCCESS, action.execute());
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
