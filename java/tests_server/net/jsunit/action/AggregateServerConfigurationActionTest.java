package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.DummyAggregateConfigurationSource;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import net.jsunit.utility.XmlUtility;

public class AggregateServerConfigurationActionTest extends TestCase {

    private AggregateServerConfigurationAction action;
    private Configuration configuration;

    public void setUp() throws Exception {
        super.setUp();
        action = new AggregateServerConfigurationAction();
        configuration = new Configuration(new DummyAggregateConfigurationSource());
        JsUnitAggregateServer server = new JsUnitAggregateServer(configuration);
        action.setAggregateServer(server);
    }

    public void testSimple() throws Exception {
        action.execute();
        String xml = XmlUtility.asString(action.getXmlRenderable().asXml());
        assertEquals(
                XmlUtility.asString(configuration.asXml(ServerType.AGGREGATE)),
                xml
        );
    }

}
