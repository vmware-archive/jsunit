package net.jsunit;

public class ServerDefaultPageFunctionalTest extends AggregateServerFunctionalTestCase {

    public void testLandingPage() throws Exception {
        webTester.beginAt("/");
        assertOnFragmentRunnerPage();
    }

    public void testLinks() throws Exception {
        webTester.beginAt("/");
        webTester.clickLinkWithText("Upload Runner");
        assertOnUploadRunnerPage();
        webTester.clickLinkWithText("URL Runner");
        assertOnUrlRunnerPage();
        webTester.clickLinkWithText("Fragment Runner");
        assertOnFragmentRunnerPage();
        webTester.clickLinkWithText("Sign up");
        assertOnMyAccountPage();
/*
        webTester.clickLinkWithText("LogDisplayer");
        assertOnLogDisplayerPage();
*/
    }

    public void testAbsoluteSlash() throws Exception {
        webTester.getTestContext().setBaseUrl("http://localhost:" + port() + "/");
        webTester.beginAt("/");
        assertOnFragmentRunnerPage();
    }

}
