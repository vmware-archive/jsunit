package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationType;
import junit.framework.TestCase;

public class JsUnitFarmServerTest extends TestCase {

	private JsUnitFarmServer server;
	
	public void setUp() throws Exception {
		super.setUp();
		server = new JsUnitFarmServer(new Configuration(new DummyFarmConfigurationSource()));
	}
	
	public void testStartTestRun() throws Exception {
		assertEquals(ConfigurationType.FARM, server.serverType());
	}
		
}