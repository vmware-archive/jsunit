package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.captcha.AesCipher;
import net.jsunit.model.ResultType;

public class CaptchaFunctionalTest extends FunctionalTestCase {

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    protected ConfigurationSource createConfigurationSource() {
        return new FunctionalTestConfigurationSource(port) {
            public String useCaptcha() {
                return String.valueOf(true);
            }
        };
    }

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/fragmentRunnerPage");
    }

    public void testHitToRunnerNotAllowed() throws Exception {
        webTester.setFormElement("fragment", "assertTrue(true)");
        webTester.submit();
        webTester.gotoFrame("resultsFrame");
        assertOnAccessDeniedPage();
    }

    public void testEnterCaptcha() throws Exception {
        webTester.assertFormElementPresent("captchaKey");
        String captchaKey = webTester.getDialog().getForm().getParameterValue("captchaKey");
        String answer = new AesCipher("1234567890123456").decrypt(captchaKey).split("_")[1];
        webTester.setFormElement("attemptedCaptchaAnswer", answer);
        webTester.setFormElement("fragment", "assertTrue(true)");
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        webTester.gotoFrame("resultsFrame");
        assertRunResult(responseXmlDocument(), ResultType.SUCCESS, null, 2);
    }

    private void assertOnAccessDeniedPage() {
        webTester.assertTextPresent("Access denied");
    }
}
