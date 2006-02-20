package net.jsunit;

import junit.framework.TestCase;

public class ClientServerInteractionTest extends TestCase {
	
	private RemoteTestRunClient client;
	private TestRunNotifierServer server;
	private MockTestRunListener mockTestRunListener;

	public void setUp() throws Exception {
		super.setUp();
		mockTestRunListener = new MockTestRunListener();
		client = new RemoteTestRunClient(mockTestRunListener, 8083);
		client.startListening();
		server = new TestRunNotifierServer(new MockBrowserTestRunner(), 8083);
		server.testRunStarted();
	}
	
	public void tearDown() throws Exception {
		server.testRunFinished();
		client.stopListening();
		super.tearDown();
	}
	
	public void testSimple() throws InterruptedException {
		
		server.browserTestRunStarted("mybrowser.exe");
		while (!mockTestRunListener.browserTestRunStartedCalled)
			Thread.sleep(3);
		assertEquals("mybrowser.exe", mockTestRunListener.browserFileName);
	}

}
