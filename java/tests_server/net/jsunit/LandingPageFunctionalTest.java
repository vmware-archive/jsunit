package net.jsunit;

import net.jsunit.utility.SystemUtility;

public class LandingPageFunctionalTest extends FunctionalTestCase {

    public void testSimple() throws Exception {
        webTester.beginAt("/index.jsp");
        webTester.assertTitleEquals("JsUnit Server");
        webTester.assertTextPresent(SystemUtility.osString());
    }

}
