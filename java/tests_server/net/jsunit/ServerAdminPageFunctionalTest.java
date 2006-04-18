package net.jsunit;

public class ServerAdminPageFunctionalTest extends FunctionalTestCase {

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("admin");
    }

    public void testInitialConditiions() throws Exception {
        webTester.assertTitleEquals("JsUnit  Server Administration");
        webTester.assertLinkPresentWithText(new FunctionalTestConfigurationSource(port).url());
    }

    public void testConfigLink() throws Exception {
        webTester.beginAt("/");
        webTester.clickLink("config");
        webTester.gotoFrame("resultsFrame");
        assertConfigXml();
    }

}
