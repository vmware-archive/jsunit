package net.jsunit;

import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultBuilder;
import net.jsunit.model.BrowserSource;

public class RemoteTestRunClient implements MessageReceiver {

    public static final String TEST_RUN_FINISHED = "testRunFinished";
    public static final String TEST_RUN_STARTED = "testRunStarted";
    public static final String BROWSER_TEST_RUN_FINISHED = "browserTestRunFinished";
    public static final String BROWSER_TEST_RUN_STARTED = "browserTestRunStarted";
    public static final String END_XML = "endXml";

    private BrowserSource browserSource;
    private final TestRunListener listener;
    private MessageReceiver complexMessageReceiver;
    private ClientSideConnection clientSideConnection;

    public RemoteTestRunClient(BrowserSource browserSource, TestRunListener listener, int serverPort) {
        this.browserSource = browserSource;
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
        if (message.equals(TEST_RUN_STARTED))
            listener.testRunStarted();
        else if (message.equals(TEST_RUN_FINISHED))
            listener.testRunFinished();
        else if (message.equals(BROWSER_TEST_RUN_STARTED))
            complexMessageReceiver = new TestRunStartedReceiver();
        else if (message.equals(BROWSER_TEST_RUN_FINISHED))
            complexMessageReceiver = new TestRunFinishedReceiver();
        else
            complexMessageReceiver.messageReceived(message);
    }

    private class TestRunStartedReceiver implements MessageReceiver {

        public void messageReceived(String browserIdString) {
            int browserId = Integer.parseInt(browserIdString);
            Browser browser = browserSource.getBrowserById(browserId);
            listener.browserTestRunStarted(browser);
        }
    }

    private class TestRunFinishedReceiver implements MessageReceiver {

        private Browser browser;
        private String xmlString = "";

        public void messageReceived(String message) {
            if (browser == null) {
                int browserId = Integer.parseInt(message);
                browser = browserSource.getBrowserById(browserId);
            } else if (message.equals(END_XML)) {
                BrowserResult result = new BrowserResultBuilder(browserSource).build(xmlString);
                listener.browserTestRunFinished(browser, result);
            } else if (message.trim().length() > 0) {
                xmlString += message;
                xmlString += "\n";
            }
        }

    }

    public void sendStopServer() {
        clientSideConnection.sendMessage("stop");
    }

}