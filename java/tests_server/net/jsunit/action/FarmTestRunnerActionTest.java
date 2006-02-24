package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.DummyConfigurationSource;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.RemoteRunnerHitter;
import net.jsunit.configuration.Configuration;
import net.jsunit.model.TestRunResult;
import org.jdom.Document;

import java.net.URL;

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
		assertTrue(action.getTestRunManager().getFarmTestRunResult().wasSuccessful());
        assertNull(action.getTestRunManager().getOverrideURL());
    }

    public void testOverrideURL() throws Exception {
        String overrideURL = "http://overrideurl.com:1234?foo=bar&bar=fo";
        action.setUrl(overrideURL);
		assertEquals(FarmTestRunnerAction.SUCCESS, action.execute());
        assertEquals(overrideURL, action.getTestRunManager().getOverrideURL());
    }

    static class SuccessfulRemoteRunnerHitter implements RemoteRunnerHitter {

		public Document hitURL(URL url) {
			return new Document(new TestRunResult().asXml());
		}
		
	}
}
