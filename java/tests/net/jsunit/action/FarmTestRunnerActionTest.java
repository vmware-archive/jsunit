package net.jsunit.action;

import java.net.URL;

import org.jdom.Document;

import junit.framework.TestCase;
import net.jsunit.DummyConfigurationSource;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.RemoteRunnerHitter;
import net.jsunit.configuration.Configuration;
import net.jsunit.model.TestRunResult;

public class FarmTestRunnerActionTest extends TestCase {

	private FarmTestRunnerAction action;

	public void setUp() throws Exception {
		super.setUp();
		action = new FarmTestRunnerAction();
		action.setFarmServer(new JsUnitFarmServer(new Configuration(new DummyConfigurationSource())));
		action.setRemoteRunnerHitter(new SuccessfulRemoteRunnerHitter());
	}
	
	public void testSimple() throws Exception {
		assertEquals(FarmTestRunnerAction.SUCCESS, action.execute());
		assertTrue(action.getTestRunManager().getTestRunResult().wasSuccessful());
	}
	
	static class SuccessfulRemoteRunnerHitter implements RemoteRunnerHitter {

		public Document hitRemoteRunner(URL url) {
			return new Document(new TestRunResult().asXml());
		}
		
	}
}
