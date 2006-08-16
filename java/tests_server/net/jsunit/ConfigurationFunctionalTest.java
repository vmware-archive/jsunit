package net.jsunit;

public class ConfigurationFunctionalTest extends ServerFunctionalTestCase {

    public void testSimple() throws Exception {
        webTester.beginAt("config");
        assertConfigXml();
    }

}
