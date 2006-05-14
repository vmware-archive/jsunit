package net.jsunit;

public class MyAccountFunctionalTest extends AggregateServerFunctionalTestCase {

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/createaccountpage");
    }

    public void testInitialConditions() throws Exception {
        assertOnSignUpPage();
    }

    public void testFields() throws Exception {
        webTester.assertFormElementPresent("user.firstName");
        webTester.assertFormElementPresent("user.lastName");
        webTester.assertFormElementPresent("user.emailAddress");
        webTester.assertFormElementPresent("password1");
        webTester.assertFormElementPresent("password2");
    }
}
