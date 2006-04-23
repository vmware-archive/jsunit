package net.jsunit;

import net.jsunit.utility.SystemUtility;

public class ServerDefaultPageFunctionalTest extends FunctionalTestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testLandingPage() throws Exception {
        webTester.beginAt("/");
        assertOnFragmentRunnerPage();
        webTester.assertTextPresent(SystemUtility.osString());
        webTester.assertTextPresent(SystemUtility.hostname());
        webTester.assertTextPresent(SystemUtility.ipAddress());
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
