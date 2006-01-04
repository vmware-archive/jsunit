package net.jsunit;

import java.util.List;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.model.BrowserResult;
import net.jsunit.plugin.eclipse.MockBrowserTestRunListener;

public class JsUnitServerTest extends TestCase {

	public void testNeedsLogging() {
		Configuration configuration = new Configuration(new DummyConfigurationSource());
		configuration.setNeedsLogging(false);
		
		List<BrowserTestRunListener> listeners = new JsUnitServer(configuration).getBrowserTestRunListeners();
		assertTrue(listeners.isEmpty());

		configuration.setNeedsLogging(true);
		listeners = new JsUnitServer(configuration).getBrowserTestRunListeners();
		assertEquals(1, listeners.size());
		assertTrue(listeners.get(0) instanceof BrowserResultLogWriter);
	}
	
	public void testLaunchingBrowser() throws JsUnitServerException {
		JsUnitServer server = new JsUnitServer(new Configuration(new DummyConfigurationSource()));
		MockProcessStarter starter = new MockProcessStarter();
		server.setProcessStarter(starter);
		MockBrowserTestRunListener listener = new MockBrowserTestRunListener();
		server.addBrowserTestRunListener(listener);
		
		server.launchTestRunForBrowserWithFileName("dummy name");
		assertTrue(listener.testRunStartedCalled);
		assertEquals(2, starter.commandPassed.length);
		assertEquals("dummy name", starter.commandPassed[0]);
		assertEquals(DummyConfigurationSource.DUMMY_URL, starter.commandPassed[1]);
		assertFalse(listener.testRunFinishedCalled);
		server.accept(new BrowserResult());
		assertTrue(listener.testRunFinishedCalled);		
	}

}