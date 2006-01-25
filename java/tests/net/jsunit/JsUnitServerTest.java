package net.jsunit;

import java.util.List;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;

public class JsUnitServerTest extends TestCase {

	public void testConfigurationDeterminesAdditionOfLogWriter() {
		Configuration configuration = new Configuration(new DummyConfigurationSource());
		configuration.setNeedsLogging(false);
		
		List<TestRunListener> listeners = new JsUnitServer(configuration).getBrowserTestRunListeners();
		assertTrue(listeners.isEmpty());

		configuration.setNeedsLogging(true);
		listeners = new JsUnitServer(configuration).getBrowserTestRunListeners();
		assertEquals(1, listeners.size());
		assertTrue(listeners.get(0) instanceof BrowserResultLogWriter);
	}
	
	public void testStartTestRun() throws Exception {
		final JsUnitServer server = new JsUnitServer(new Configuration(new DummyConfigurationSource()));
		server.setProcessStarter(new MockProcessStarter());
		MockTestRunListener listener = new MockTestRunListener();
		server.addBrowserTestRunListener(listener);
		Thread thread = new Thread() {
			public void run() {
				try {
					server.startTestRun();
				} catch (Exception e) {
					fail(e.toString());
				}
			}
		};
		thread.start();
		Thread.sleep(500);
		assertTrue(thread.isAlive());
		listener.isReady = true;
		thread.join();		
	}
	
	public void testLaunchingBrowser() throws FailedToLaunchBrowserException {
		JsUnitServer server = new JsUnitServer(new Configuration(new DummyConfigurationSource()));
		MockProcessStarter mockProcessStarter = new MockProcessStarter();
		server.setProcessStarter(mockProcessStarter);
		MockTestRunListener listener = new MockTestRunListener();
		server.addBrowserTestRunListener(listener);
		
		server.launchTestRunForBrowserWithFileName("dummy name");
		assertTrue(listener.browserTestRunStartedCalled);
		assertEquals(2, mockProcessStarter.commandPassed.length);
		assertEquals("dummy name", mockProcessStarter.commandPassed[0]);
		assertEquals(DummyConfigurationSource.DUMMY_URL, mockProcessStarter.commandPassed[1]);
		assertFalse(listener.testRunFinishedCalled);
		server.accept(new DummyBrowserResult(true, 0, 0));
		assertTrue(listener.browserTestRunFinishedCalled);		
	}
	
	public void testStartEnd() {
		final JsUnitServer server = new JsUnitServer(new Configuration(new DummyConfigurationSource()));
		server.setProcessStarter(new MockProcessStarter());
		MockTestRunListener listener = new MockTestRunListener();
		listener.isReady = true;
		server.addBrowserTestRunListener(listener);
		server.startTestRun();
		assertTrue(listener.testRunStartedCalled);
		server.finishTestRun();
		assertTrue(listener.testRunFinishedCalled);
	}

}