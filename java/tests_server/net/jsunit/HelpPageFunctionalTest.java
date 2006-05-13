package net.jsunit;

public class HelpPageFunctionalTest extends AggregateServerFunctionalTestCase {

    public void testSimple() throws Exception {
        webTester.beginAt("/");
        webTester.clickLinkWithText("Help");
    }

}
