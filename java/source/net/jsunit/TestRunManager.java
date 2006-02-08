package net.jsunit;

import net.jsunit.configuration.Configuration;

public class TestRunManager {

    private BrowserTestRunner testRunner;
    private TestRunResult testRunResult = new TestRunResult();

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
	            testRunResult.addBrowserResult(testRunner.lastResult());
	        }
		} finally {
			testRunner.finishTestRun();
		}
	}

    public boolean hadProblems() {
        return !testRunResult.wasSuccessful();
    }

    private void waitForResultToBeSubmitted(String browserFileName, long launchTime) throws Exception {
        testRunner.logStatus("Waiting for " + browserFileName + " to submit result");
        long secondsWaited = 0;
        while (!testRunner.hasReceivedResultSince(launchTime)) {
            Thread.sleep(1000);
            secondsWaited++;
            if (secondsWaited > (testRunner.timeoutSeconds())+3)
                throw new RuntimeException("Server not responding");
        }
    }

    public int getFailureCount() {
        return testRunResult.getFailureCount();
    }

    public int getErrorCount() {
        return testRunResult.getErrorCount();
    }

}