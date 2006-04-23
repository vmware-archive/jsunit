package net.jsunit;

import net.jsunit.utility.SystemUtility;

public class ServerDefaultPageFunctionalTest extends FunctionalTestCase {

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/");
    }

    public void testLandingPage() throws Exception {
        assertOnFragmentRunnerPage();
        webTester.assertTextPresent(SystemUtility.osString());
        webTester.assertTextPresent(SystemUtility.hostname());
        webTester.assertTextPresent(SystemUtility.ipAddress());
    }

    public void testLinks() throws Exception {
        webTester.clickLinkWithText("UploadRunner");
        assertOnUploadRunnerPage();
        webTester.clickLinkWithText("URLRunner");
        assertOnUrlRunnerPage();
        webTester.clickLinkWithText("LogDisplayer");
        assertOnLogDisplayerPage();
    }

}
