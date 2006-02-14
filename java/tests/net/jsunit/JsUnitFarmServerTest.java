package net.jsunit;

import net.jsunit.configuration.FarmConfiguration;
import junit.framework.TestCase;

public class JsUnitFarmServerTest extends TestCase {

	private JsUnitFarmServer server;
	
	public void setUp() throws Exception {
		super.setUp();
		server = new JsUnitFarmServer(new FarmConfiguration(new DummyFarmConfigurationSource()));
	}
	
	public void testStartTestRun() throws Exception {
		assertEquals(2, server.getRemoteMachineURLs().size());
	}
		
}