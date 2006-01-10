package net.jsunit;

import java.util.Date;

import net.jsunit.model.BrowserResult;

public class TestRunManager {

    public static final int MAX_SECONDS_TO_WAIT = 60;
	private int errorCount;
	private int failureCount;

	private BrowserTestRunner testRunner;

	public TestRunManager(BrowserTestRunner testRunner) {
		this.testRunner = testRunner;
	}

	public void runTests() throws Exception {
        for (String browserFileName : testRunner.getBrowserFileNames()) {
            Date dateLaunched = new Date();
            testRunner.launchTestRunForBrowserWithFileName(browserFileName);
            waitForResultToBeSubmitted(browserFileName, dateLaunched);
            verifyLastResult();
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
