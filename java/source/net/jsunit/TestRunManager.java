package net.jsunit;

import java.util.Date;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.BrowserResult;

public class TestRunManager {

    public static final int MAX_SECONDS_TO_WAIT = 60;
	private int errorCount;
	private int failureCount;

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
	
	private BrowserTestRunner testRunner;

	public TestRunManager(BrowserTestRunner testRunner) {
		this.testRunner = testRunner;
	}

	public void runTests() throws Exception {
		testRunner.startTestRun();
		try {
	        for (String browserFileName : testRunner.getBrowserFileNames()) {
	            Date dateLaunched = new Date();
	            testRunner.launchTestRunForBrowserWithFileName(browserFileName);
	            waitForResultToBeSubmitted(browserFileName, dateLaunched);
	            verifyLastResult();
	        }
		} finally {
			testRunner.finishTestRun();
		}
	}

	public boolean hadProblems() {
		return errorCount>0 || failureCount>0;
	}

    private void waitForResultToBeSubmitted(String browserFileName, Date dateBrowserLaunched) throws Exception {
        Utility.log("Waiting for " + browserFileName + " to submit result");
        long secondsWaited = 0;
        while (!testRunner.hasReceivedResultSince(dateBrowserLaunched)) {
            Thread.sleep(1000);
            secondsWaited++;
            if (secondsWaited > MAX_SECONDS_TO_WAIT)
                throw new RuntimeException("Waited more than " + MAX_SECONDS_TO_WAIT + " seconds for browser " + browserFileName);
        }
    }

    private void verifyLastResult() {
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
