package net.jsunit.action;

import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.configuration.AggregateConfiguration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.utility.XmlUtility;

public class AggregateConfigurationActionTest extends TestCase {

    public void testSimple() throws Exception {
        AggregateConfiguration configuration = new AggregateConfiguration(new DummyConfigurationSource());
        AggregateConfigurationAction action = new AggregateConfigurationAction();
        action.setAggregateServer(new JsUnitAggregateServer(configuration));

        assertEquals(Action.SUCCESS, action.execute());
        assertEquals(XmlUtility.asPrettyString(configuration.asXml()), XmlUtility.asPrettyString(action.asXml()));
    }
}
