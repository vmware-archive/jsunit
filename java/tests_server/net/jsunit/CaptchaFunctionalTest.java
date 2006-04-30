package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class CaptchaFunctionalTest extends FunctionalTestCase {

    protected ConfigurationSource createConfigurationSource() {
        return new FunctionalTestConfigurationSource(port) {
            public String useCaptcha() {
                return "true";
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
        assertOnAccessDeniedPage();
    }

    public void testEnterCaptcha() throws Exception {
        webTester.assertFormElementPresent("captchaKey");
        webTester.setFormElement("attemptedCaptchaAnswer", "theAnswer");
        webTester.setFormElement("fragment", "assertTrue(true)");
        webTester.submit();
        assertOnAccessDeniedPage();
    }

    private void assertOnAccessDeniedPage() {
        webTester.assertTitleEquals("Access Denied - JsUnit");
    }
}
