package net.jsunit;

import junit.framework.TestCase;

public class ClientServerInteractionTest extends TestCase {
	
	public void testSimple() throws InterruptedException {
		MockTestRunListener mockTestRunListener = new MockTestRunListener();
		RemoteTestRunClient client = new RemoteTestRunClient(mockTestRunListener, 8083);
		TestRunNotifierServer server = new TestRunNotifierServer(8083);
		client.startListening();
		server.testRunStarted();
		
		server.browserTestRunStarted("mybrowser.exe");
		while (!mockTestRunListener.browserTestRunStartedCalled)
			Thread.sleep(100);
		assertEquals("mybrowser.exe", mockTestRunListener.browserFileName);
	}

}
