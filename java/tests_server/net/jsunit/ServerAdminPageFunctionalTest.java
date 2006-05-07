package net.jsunit;

public class ServerAdminPageFunctionalTest extends StandardServerFunctionalTestCase {

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("admin");
    }

    public void testInitialConditiions() throws Exception {
        webTester.assertTitleEquals("Server Administration - JsUnit");
        webTester.assertLinkPresentWithText(new FunctionalTestConfigurationSource(port).url());
    }

}
