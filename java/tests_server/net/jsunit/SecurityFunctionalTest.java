package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class SecurityFunctionalTest extends FunctionalTestCase {

    protected ConfigurationSource createConfigurationSource() {
        return new FunctionalTestConfigurationSource(port) {
            public String runnerReferrerRestrict() {
                return "http://www.jsunit.net";
            }
        };
    }

    public void testHitToRunnerNotAllowed() throws Exception {
        webTester.beginAt("/fragmentRunnerPage");
        webTester.setFormElement("fragment", "assertTrue(true)");
        webTester.submit();
        assertOnAccessDeniedPage();
    }

    private void assertOnAccessDeniedPage() {
        webTester.assertTitleEquals("Access Denied - JsUnit");
    }
}
