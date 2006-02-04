package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.BrowserResult;

public class TestRunManager {

    private int errorCount;
	private int failureCount;
	private BrowserTestRunner testRunner;

	public static void main(String[] args) throws Exception {
		JsUnitServer server = new JsUnitServer(Configuration.resolve(args));
		int port = Integer.parseInt(args[args.length - 1]);
		server.addBrowserTestRunListener(new TestRunNotifierServer(port));
		server.start();
		TestRunManager manager = new TestRunManager(server);
		manager.runTests();
		server.dispose();
		System.exit(manager.hadProblems() ? 1 : 0);
	}
	
	public TestRunManager(BrowserTestRunner testRunner) {
		this.testRunner = testRunner;
	}

	public void runTests() throws Exception {
		testRunner.startTestRun();
		try {
	        for (String browserFileName : testRunner.getBrowserFileNames()) {
	            long launchTime = testRunner.launchTestRunForBrowserWithFileName(browserFileName);
	            waitForResultToBeSubmitted(browserFileName, launchTime);
	            updateFromLastResult();
	        }
		} finally {
			testRunner.finishTestRun();
		}
	}

	public boolean hadProblems() {
		return errorCount>0 || failureCount>0;
	}

    private void waitForResultToBeSubmitted(String browserFileName, long launchTime) throws Exception {
        testRunner.logStatus("Waiting for " + browserFileName + " to submit result");
        long secondsWaited = 0;
        while (!testRunner.hasReceivedResultSince(launchTime)) {
            Thread.sleep(1000);
            secondsWaited++;
            if (secondsWaited > JsUnitServer.MAX_SECONDS_TO_WAIT + 10)
                throw new RuntimeException("Server not responding");
        }
    }

    private void updateFromLastResult() {
        BrowserResult result = testRunner.lastResult();
        if (!result.wasSuccessful()) {
        	errorCount += result.errorCount();
        	failureCount += result.failureCount();
        }
    }

	public int getFailureCount() {
		return failureCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

}
