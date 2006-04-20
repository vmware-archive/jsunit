package net.jsunit;

public class ConfigurationPageFunctionalTest extends FunctionalTestCase {

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/configurationPage");
    }

    public void testInitialConditions() throws Exception {
        assertOnConfigurationPage();
    }

}
