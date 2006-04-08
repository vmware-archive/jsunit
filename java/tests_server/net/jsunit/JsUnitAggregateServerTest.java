package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;

public class JsUnitAggregateServerTest extends TestCase {

    private JsUnitAggregateServer server;

    public void setUp() throws Exception {
        super.setUp();
        server = new JsUnitAggregateServer(new Configuration(new DummyAggregateConfigurationSource()));
    }

    public void testStartTestRun() throws Exception {
        assertEquals(ServerType.AGGREGATE, server.serverType());
    }

}