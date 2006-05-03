package net.jsunit;

public class ServerDefaultPageFunctionalTest extends FunctionalTestCase {

    public void testLandingPage() throws Exception {
        webTester.beginAt("/");
        assertOnFragmentRunnerPage();

        webTester.assertTextPresent(FunctionalTestConfigurationSource.OS_STRING);
        webTester.assertTextPresent(FunctionalTestConfigurationSource.HOSTNAME);
        webTester.assertTextPresent(FunctionalTestConfigurationSource.IP_ADDRESS);
    }

    public void testLinks() throws Exception {
        webTester.beginAt("/");
        webTester.clickLinkWithText("UploadRunner");
        assertOnUploadRunnerPage();
        webTester.clickLinkWithText("URLRunner");
        assertOnUrlRunnerPage();
        webTester.clickLinkWithText("LogDisplayer");
        assertOnLogDisplayerPage();
    }

    public void testAbsoluteSlash() throws Exception {
        webTester.getTestContext().setBaseUrl("http://localhost:" + port + "/");
        webTester.beginAt("/");
        assertOnFragmentRunnerPage();
    }

}
