package net.jsunit;

import net.jsunit.utility.SystemUtility;

public class LandingPageFunctionalTest extends FunctionalTestCase {

    public void testSlash() throws Exception {
        webTester.beginAt("/");
        assertOnLandingPage();
        webTester.assertTextPresent(SystemUtility.osString());
        webTester.assertTextPresent(SystemUtility.hostname());
        webTester.assertTextPresent(SystemUtility.ipAddress());
        webTester.assertLinkPresentWithText(new FunctionalTestConfigurationSource(PORT).url());
    }

    public void testIndexDotJsp() throws Exception {
        webTester.beginAt("/index.jsp");
        assertOnLandingPage();
    }

    public void testConfigLink() throws Exception {
        webTester.beginAt("/");
        webTester.clickLinkWithText("config");
        assertConfigXml();
    }

    private void assertOnLandingPage() {
        webTester.assertTitleEquals("JsUnit  Server");
    }

}
