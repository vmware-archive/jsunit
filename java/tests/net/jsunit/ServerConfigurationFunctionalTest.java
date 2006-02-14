package net.jsunit;

import org.jdom.Document;
import org.jdom.Element;

public class ServerConfigurationFunctionalTest extends ServerFunctionalTestCase {

    public void testSimple() throws Exception {
        webTester.beginAt("config");
        Document result = responseXmlDocument();
        Element root = result.getRootElement();
        assertEquals("configuration", root.getName());
    }

}
