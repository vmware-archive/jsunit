package net.jsunit;

import org.jdom.JDOMException;

import java.io.IOException;

public class AggregateConfigurationFunctionalTest extends AggregateServerFunctionalTestCase {

    public void testSimple() throws IOException, JDOMException {
        webTester.beginAt("/config");
        assertConfigXml();
    }

}
