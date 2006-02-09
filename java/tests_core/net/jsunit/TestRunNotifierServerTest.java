package net.jsunit;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestRunNotifierServerTest extends TestCase implements MessageReceiver {

	private TestRunNotifierServer server;
	private ClientSideConnection clientSideConnection;
	private List<String> messages = new ArrayList<String>();
	private MockBrowserTestRunner mockRunner;

	public void setUp() throws Exception {
		super.setUp();
		mockRunner = new MockBrowserTestRunner();
		server = new TestRunNotifierServer(mockRunner, 8083);
		clientSideConnection = new ClientSideConnection(this, 8083);
		new Thread() {
			public void run() {
				server.testRunStarted();				
			}
		}.start();
		
		clientSideConnection.start();
		waitForServerConnectionToStartRunning();
	}

	public void testInitialConditions() {
		assertTrue(server.isReady());
		
		assertEquals(1, messages.size());
		assertEquals("testRunStarted", ndLastMessage(0));
	}
	
	public void testMessagesSentAsTestRunProceeds() throws InterruptedException {
		server.browserTestRunStarted("mybrowser1.exe");
		while (messages.size()<2) {
			Thread.sleep(100);
		}
		assertEquals("browserTestRunStarted", ndLastMessage(1));
		assertEquals("mybrowser1.exe", ndLastMessage(0));

		DummyBrowserResult browserResult = new DummyBrowserResult(false, 2, 3);
		server.browserTestRunFinished("mybrowser2.exe", browserResult);
		while (messages.size()<2+3) {
			Thread.sleep(100);
		}
		assertEquals("browserTestRunFinished", ndLastMessage(5));
		assertEquals("mybrowser2.exe", ndLastMessage(4));
		String line1 = ndLastMessage(3);
		String line2 = ndLastMessage(2);
		String line3 = ndLastMessage(1);
		assertEquals(Utility.asString(browserResult.asXmlDocument()), line1 + "\r\n" + line2 + "\r\n" + line3);
		
		assertEquals("endXml", ndLastMessage(0));
	}
	
	public void testStopRunner() {
		assertFalse(mockRunner.disposeCalled);
		clientSideConnection.sendMessage("foo");
		assertFalse(mockRunner.disposeCalled);
		clientSideConnection.sendMessage("stop");
		assertTrue(mockRunner.disposeCalled);
	}

	private String ndLastMessage(int count) {
		return messages.get(messages.size() - (count + 1));
	}
	
	private void waitForServerConnectionToStartRunning() throws InterruptedException {
		while (!clientSideConnection.isRunning())
			Thread.sleep(100);		
	}

	public void messageReceived(String message) {
		messages.add(message);
	}
	
	public void tearDown() throws Exception {
		server.testRunFinished();
		clientSideConnection.shutdown();
		super.tearDown();
	}	
}
