package net.jsunit;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import net.jsunit.model.BrowserResult;

public class TestRunNotifierServer implements TestRunListener {

	public static final String TEST_RUN_FINISHED = "testRunFinished";
	public static final String TEST_RUN_STARTED = "testRunStarted";
	public static final String BROWSER_TEST_RUN_FINISHED = "browserTestRunFinished";
	public static final String BROWSER_TEST_RUN_STARTED = "browserTestRunStarted";
	public static final String END_XML = "endXml";
	
	private int port;
	private Socket clientSocket;
	private PrintWriter writer;
	private String host= "localhost";
	private boolean isConnected;

	public TestRunNotifierServer(int port) {
		this.port = port;
	}
	
	public void browserTestRunStarted(String browserFileName) {
		sendMessage(BROWSER_TEST_RUN_STARTED);
		sendMessage(browserFileName);
		writer.flush();
	}

	public void browserTestRunFinished(String browserFileName, BrowserResult result) {
		sendMessage(BROWSER_TEST_RUN_FINISHED);
		sendMessage(browserFileName);
		sendMessage(Utility.asString(result.asXmlDocument()));
		sendMessage(END_XML);
		writer.flush();
	}
		
	public void testRunStarted() {
		connect();
		sendMessage(TEST_RUN_STARTED);
		writer.flush();
	}

	public void testRunFinished() {
		sendMessage(TEST_RUN_FINISHED);
		writer.flush();
		shutDown();
	}

	private void connect() {
		for (int i = 1; i < 30; i++) {
			try{
				clientSocket = new Socket(host, port);
			    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8")), false);
				isConnected = true;
				return;
			} catch(IOException e1){
				try {
					Thread.sleep(250);
				} catch(InterruptedException e2) {
				}
			}
		}
		throw new RuntimeException("server could not connect");
	}

	private void shutDown() {
		if (writer != null) {
			writer.close();
			writer= null;
		}
		
		try {
			if (clientSocket != null) {
				clientSocket.close();
				clientSocket= null;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(String message) {
		if(writer == null) 
			return;
		writer.println(message);
	}

	public boolean isReady() {
		return isConnected;
	}

}
