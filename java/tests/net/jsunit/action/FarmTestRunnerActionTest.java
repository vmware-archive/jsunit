package net.jsunit.action;

import net.jsunit.DummyConfigurationSource;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.configuration.Configuration;
import junit.framework.TestCase;

public class FarmTestRunnerActionTest extends TestCase {

	private FarmTestRunnerAction action;

	public void setUp() throws Exception {
		super.setUp();
		action = new FarmTestRunnerAction();
		action.setFarmServer(new JsUnitFarmServer(new Configuration(new DummyConfigurationSource())));
	}
	
	public void testSimple() throws Exception {
		
		assertEquals(FarmTestRunnerAction.SUCCESS, action.execute());
	}
	
}
