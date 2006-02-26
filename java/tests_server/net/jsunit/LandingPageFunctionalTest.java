package net.jsunit;

import net.jsunit.utility.SystemUtility;

public class LandingPageFunctionalTest extends FunctionalTestCase {

    public void testSimple() throws Exception {
        webTester.beginAt("/index.jsp");
        webTester.assertTitleEquals("JsUnit 2.2 Server");
        webTester.assertTextPresent(SystemUtility.osString());
    }

}
