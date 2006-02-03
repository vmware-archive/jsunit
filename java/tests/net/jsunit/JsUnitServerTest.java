package net.jsunit;

import java.util.List;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;

public class JsUnitServerTest extends TestCase {

	private JsUnitServer server;
	
	public void setUp() throws Exception {
		super.setUp();
		server = new JsUnitServer(new Configuration(new DummyConfigurationSource()));
	}
	
	public void testStartTestRun() throws Exception {
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
	
	public void testLaunchingBrowser()  {
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
		server.setProcessStarter(new MockProcessStarter());
		MockTestRunListener listener = new MockTestRunListener();
		listener.isReady = true;
		server.addBrowserTestRunListener(listener);
		server.startTestRun();
		assertTrue(listener.testRunStartedCalled);
		server.finishTestRun();
		assertTrue(listener.testRunFinishedCalled);
	}
	
	public void testConfigurationDeterminesAdditionOfLogWriter() {
		Configuration configuration = new Configuration(new DummyConfigurationSource() {
			public String logStatus() {
				return String.valueOf(false);
			}
		});
		
		List<TestRunListener> listeners = new JsUnitServer(configuration).getBrowserTestRunListeners();
		assertTrue(listeners.isEmpty());

		configuration = new Configuration(new DummyConfigurationSource() {
			public String logStatus() {
				return String.valueOf(true);
			}
		});
		listeners = new JsUnitServer(configuration).getBrowserTestRunListeners();
		assertEquals(1, listeners.size());
		assertTrue(listeners.get(0) instanceof BrowserResultLogWriter);
	}
	
}