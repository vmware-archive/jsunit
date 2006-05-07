package net.jsunit;

public class HelpPageFunctionalTest extends StandardServerFunctionalTestCase {

    public void testSimple() throws Exception {
        webTester.beginAt("/");
        webTester.clickLinkWithText("Help");
    }

}
