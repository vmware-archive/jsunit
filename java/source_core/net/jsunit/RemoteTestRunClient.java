package net.jsunit;


import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultBuilder;

public class RemoteTestRunClient implements MessageReceiver {

	private final TestRunListener listener;
	private MessageReceiver complexMessageReceiver;
	private ClientSideConnection clientSideConnection;

	public RemoteTestRunClient(TestRunListener listener, int serverPort) {
		this.listener = listener;
		clientSideConnection = new ClientSideConnection(this, serverPort);
	}
	
	public void startListening() {
		clientSideConnection.start();
	}
	
	public void stopListening() {
		clientSideConnection.shutdown();
	}

	public void messageReceived(String message) {
		if (message.equals(TestRunNotifierServer.TEST_RUN_STARTED))
			listener.testRunStarted();
		else if (message.equals(TestRunNotifierServer.TEST_RUN_FINISHED))
			listener.testRunFinished();
		else if (message.equals(TestRunNotifierServer.BROWSER_TEST_RUN_STARTED))
			complexMessageReceiver = new TestRunStartedReceiver();
		else if (message.equals(TestRunNotifierServer.BROWSER_TEST_RUN_FINISHED))
			complexMessageReceiver = new TestRunFinishedReceiver();
		else
			complexMessageReceiver.messageReceived(message);
	}
	
	private class TestRunStartedReceiver implements MessageReceiver {

		public void messageReceived(String browserFileName) {
			listener.browserTestRunStarted(browserFileName);
		}
	}
	
	private class TestRunFinishedReceiver implements MessageReceiver {

		private String browserFileName;
		private String xmlString = "";
		
		public void messageReceived(String message) {
			if (browserFileName == null)
				browserFileName = message;
			else if (message.equals(TestRunNotifierServer.END_XML)) {
				BrowserResult result = new BrowserResultBuilder().build(xmlString);
				listener.browserTestRunFinished(browserFileName, result);
			} else if (message.trim().length() > 0){
				xmlString += message;
				xmlString += "\n";
			}
		}
		
	}

	public void sendStopServer() {
		clientSideConnection.sendMessage("stop");
	}

}