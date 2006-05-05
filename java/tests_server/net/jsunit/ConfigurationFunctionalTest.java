package net.jsunit;

public class ConfigurationFunctionalTest extends StandardServerFunctionalTestCase {

    public void testSimple() throws Exception {
        webTester.beginAt("config");
        assertConfigXml();
    }

}
