package net.jsunit;

import java.net.URLEncoder;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.FarmConfiguration;

public class FarmFunctionalTest extends FunctionalTestCase {

	private JsUnitServer server1;
	private JsUnitServer server2;
	private JsUnitFarmServer farmServer;

	public void setUp() throws Exception {
		super.setUp();
		server1 = new JsUnitServer(new Configuration(new FunctionalTestConfigurationSource(PORT + 1)));
		server1.start();
		server2 = new JsUnitServer(new Configuration(new FunctionalTestConfigurationSource(PORT + 2)));
		server2.start();
		farmServer = new JsUnitFarmServer(new FarmConfiguration(new FunctionalTestFarmConfigurationSource(PORT + 1, PORT + 2)));
		farmServer.start();
	}
	
	public void testHitRunner() throws Exception {
		webTester.beginAt("/runner?url=" + URLEncoder.encode("http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html", "UTF-8"));
	}
	
	public void tearDown() throws Exception {
		server1.dispose();
		server2.dispose();
		farmServer.dispose();
		super.tearDown();
	}

}
