package net.jsunit;

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
		MockProcessStarter starter = new MockProcessStarter();
		server.setProcessStarter(starter);
		MockTestRunListener listener = new MockTestRunListener();
		server.addBrowserTestRunListener(listener);
		
		server.launchTestRunForBrowserWithFileName("mybrowser.exe");
		assertTrue(listener.browserTestRunStartedCalled);
		assertEquals(2, starter.commandPassed.length);
		assertEquals("mybrowser.exe", starter.commandPassed[0]);
		assertEquals(DummyConfigurationSource.DUMMY_URL, starter.commandPassed[1]);
		assertFalse(listener.testRunFinishedCalled);
		server.accept(new DummyBrowserResult(true, 0, 0));
		assertTrue(listener.browserTestRunFinishedCalled);		
	}
	
	public void testLaunchingBrowserCrashes() throws InterruptedException {
		BlowingUpProcessStarter starter = new BlowingUpProcessStarter();
		server.setProcessStarter(starter);
		MockTestRunListener listener = new MockTestRunListener();
		server.addBrowserTestRunListener(listener);
		
		long launchTime = server.launchTestRunForBrowserWithFileName("mybrowser.exe");
		assertTrue(listener.browserTestRunStartedCalled);
		assertTrue(listener.browserTestRunFinishedCalled);
		assertTrue(listener.result.failedToLaunch());
		assertTrue(server.hasReceivedResultSince(launchTime));
		assertEquals("mybrowser.exe", listener.browserFileName);
		assertEquals("mybrowser.exe", listener.result.getBrowserFileName());
		assertSame(listener.result, server.lastResult());
		
		server.setProcessStarter(new MockProcessStarter());
		listener.reset();
		launchTime = server.launchTestRunForBrowserWithFileName("mybrowser2.exe");
		assertFalse(server.hasReceivedResultSince(launchTime));
		assertTrue(listener.browserTestRunStartedCalled);
		assertFalse(listener.browserTestRunFinishedCalled);
		assertEquals("mybrowser2.exe", listener.browserFileName);
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

}