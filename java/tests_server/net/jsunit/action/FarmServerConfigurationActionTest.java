package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.DummyFarmConfigurationSource;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import net.jsunit.utility.XmlUtility;

public class FarmServerConfigurationActionTest extends TestCase {

    private FarmServerConfigurationAction action;
    private Configuration configuration;

    public void setUp() throws Exception {
        super.setUp();
        action = new FarmServerConfigurationAction();
        configuration = new Configuration(new DummyFarmConfigurationSource());
        JsUnitFarmServer server = new JsUnitFarmServer(configuration);
        action.setFarmServer(server);
    }

    public void testSimple() throws Exception {
        action.execute();
        String xml = XmlUtility.asString(action.getXmlRenderable().asXml());
        assertEquals(
                XmlUtility.asString(configuration.asXml(ServerType.FARM)),
                xml
        );
    }

}
