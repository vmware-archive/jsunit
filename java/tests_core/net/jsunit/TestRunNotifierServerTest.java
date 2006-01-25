package net.jsunit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestRunNotifierServerTest extends TestCase {

	private TestRunNotifierServer server;
	private ServerConnection serverConnection;
	private List<String> messages = new ArrayList<String>();

	public void setUp() throws Exception {
		super.setUp();
		server = new TestRunNotifierServer(8083);
		serverConnection = new ServerConnection();
	}
	
	public void testSimple() throws InterruptedException {
		assertFalse(server.isReady());
		new Thread() {
			public void run() {
				server.testRunStarted();				
			}
		}.start();
		assertFalse(server.isReady());
		
		serverConnection.start();
		waitForServerConnectionToStartRunning();
		assertTrue(server.isReady());
		
		assertEquals(1, messages.size());
		assertEquals("testRunStarted", ndLastMessage(0));

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

	private String ndLastMessage(int count) {
		return messages.get(messages.size() - (count + 1));
	}
	
	private void waitForServerConnectionToStartRunning() throws InterruptedException {
		while (!serverConnection.running)
			Thread.sleep(100);		
	}

	private void receivedMessage(String message) {
		messages.add(message);
	}
	
	public void tearDown() throws Exception {
		server.testRunFinished();
		serverConnection.shutdown();
		super.tearDown();
	}
	
	private class ServerConnection extends Thread {
		private ServerSocket serverSocket;
		private Socket socket;
		private BufferedReader bufferedReader;
		private boolean running;
		
		public void run() {
			try {
				serverSocket = new ServerSocket(8083);
				socket= serverSocket.accept();
			    bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				String message;
				running = true;
				while(running && bufferedReader != null && (message= bufferedReader.readLine()) != null)
					receivedMessage(message);
			} catch (Exception e) {
				fail(e.getMessage());
			}
		}

		public void shutdown() {
			running = false;
		}

	}
	
}
