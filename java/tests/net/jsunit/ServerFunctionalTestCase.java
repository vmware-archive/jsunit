package net.jsunit;

import net.jsunit.configuration.Configuration;

public abstract class ServerFunctionalTestCase extends FunctionalTestCase {

	protected JsUnitServer server;

	public void setUp() throws Exception {
		super.setUp();
		Configuration configuration = new Configuration(new FunctionalTestConfigurationSource(PORT));
		server = new JsUnitServer(configuration);
		server.start();
	}
	
	public void tearDown() throws Exception {
		server.dispose();
		super.tearDown();
	}
	
}
