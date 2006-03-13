package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.TestRunResult;

public class TestRunManager {

    private BrowserTestRunner testRunner;
    private TestRunResult testRunResult;
    private final String overrideUrl;

    public static void main(String[] args) throws Exception {
        JsUnitStandardServer server = new JsUnitStandardServer(Configuration.resolve(args));
        server.setTemporary(true);
        int port = Integer.parseInt(args[args.length - 1]);
        server.addBrowserTestRunListener(new TestRunNotifierServer(server, port));
        server.start();
        TestRunManager manager = new TestRunManager(server);
        manager.runTests();
        if (server.isAlive())
            server.dispose();
    }

    public TestRunManager(BrowserTestRunner testRunner) {
        this(testRunner, null);
    }

    public TestRunManager(BrowserTestRunner testRunner, String overrideUrl) {
        this.testRunner = testRunner;
        this.overrideUrl = overrideUrl;
    }

    public void runTests() {
        initializeTestRunResult();
        testRunner.logStatus("Starting Test Run");
        testRunner.startTestRun();
        try {
            for (String browserFileName : testRunner.getBrowserFileNames()) {
                BrowserLaunchSpecification launchSpec = new BrowserLaunchSpecification(browserFileName, overrideUrl);
                long launchTime = testRunner.launchBrowserTestRun(launchSpec);
                waitForResultToBeSubmitted(browserFileName, launchTime);
                if (testRunner.isAlive())
                    testRunResult.addBrowserResult(testRunner.lastResult());
                else
                    return;
            }
        } finally {
            testRunner.finishTestRun();
        }
        testRunner.logStatus("Test Run Completed");
    }

    private void initializeTestRunResult() {
        testRunResult = new TestRunResult();
        testRunResult.initializeProperties();
    }

    private void waitForResultToBeSubmitted(String browserFileName, long launchTime) {
        testRunner.logStatus("Waiting for " + browserFileName + " to submit result");
        long secondsWaited = 0;
        while (testRunner.isAlive() && !testRunner.hasReceivedResultSince(launchTime)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            secondsWaited++;
            if (secondsWaited > (testRunner.timeoutSeconds()) + 3)
                throw new RuntimeException("Server not responding");
        }
    }

    public TestRunResult getTestRunResult() {
        return testRunResult;
    }

}