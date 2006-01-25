package net.jsunit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdom.Document;

import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultBuilder;

public class RemoteTestRunClient {

	private final TestRunListener listener;
	private ComplexMessageReceiver receiver;
	private ServerConnection serverConnection;

	public RemoteTestRunClient(TestRunListener listener) {
		this.listener = listener;
	}
	
	public void startListening() {
		serverConnection = new ServerConnection();
		serverConnection.start();
	}

	public void messageReceived(String message) {
		System.out.println(message);
		if (message.equals(TestRunNotifierServer.TEST_RUN_STARTED))
			listener.testRunStarted();
		else if (message.equals(TestRunNotifierServer.TEST_RUN_FINISHED))
			listener.testRunFinished();
		else if (message.equals(TestRunNotifierServer.BROWSER_TEST_RUN_STARTED))
			receiver = new TestRunStartedReceiver();
		else if (message.equals(TestRunNotifierServer.BROWSER_TEST_RUN_FINISHED))
			receiver = new TestRunFinishedReceiver();
		else
			receiver.messageReceived(message);
	}
	
	private interface ComplexMessageReceiver {
		public void messageReceived(String message);
	}
	
	private class TestRunStartedReceiver implements ComplexMessageReceiver {

		public void messageReceived(String browserFileName) {
			listener.browserTestRunStarted(browserFileName);
		}
	}
	
	private class TestRunFinishedReceiver implements ComplexMessageReceiver {

		private String browserFileName;
		private String xmlString = "";
		
		public void messageReceived(String message) {
			if (browserFileName == null)
				browserFileName = message;
			else if (message.equals(TestRunNotifierServer.END_XML)) {
				Document document = Utility.asXmlDocument(xmlString);
				BrowserResult result = new BrowserResultBuilder().build(document);
				listener.browserTestRunFinished(browserFileName, result);
			} else if (message.trim().length() > 0){
				xmlString += message;
				xmlString += "\r\n";
			}
		}
		
	}
	
	private class ServerConnection extends Thread {
		private ServerSocket serverSocket;
		private Socket socket;
		private BufferedReader bufferedReader;
		private boolean running;
		
		public void run() {
			try {
				serverSocket = new ServerSocket(8083);
				socket = serverSocket.accept();
			    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				String message;
				running = true;
				while(running && bufferedReader != null && (message = bufferedReader.readLine()) != null)
					messageReceived(message);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			shutdown();
		}

		public void shutdown() {
			try {
				serverSocket.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			running = false;
		}

	}

}