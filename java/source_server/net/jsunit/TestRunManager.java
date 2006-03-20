package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.TestRunResult;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestRunManager {

    private BrowserTestRunner testRunner;
    private TestRunResult testRunResult;
    private final String overrideUrl;
    private List<String> browserFileNames;

    public static void main(String[] args) throws Exception {
        JsUnitStandardServer server = new JsUnitStandardServer(Configuration.resolve(args), true);
        int port = Integer.parseInt(args[args.length - 1]);
        if (noLogging(args))
            shutOffAllLogging();
        server.addBrowserTestRunListener(new TestRunNotifierServer(server, port));
        server.start();
        TestRunManager manager = new TestRunManager(server);
        manager.runTests();
        if (server.isAlive())
            server.dispose();
    }

    private static void shutOffAllLogging() {
        Logger.getLogger("net.jsunit").setLevel(Level.OFF);
        Logger.getLogger("org.mortbay").setLevel(Level.OFF);
        Logger.getLogger("com.opensymphony").setLevel(Level.OFF);
    }

    private static boolean noLogging(String[] arguments) {
        for (String string : arguments)
            if (string.equals("-noLogging"))
                return true;
        return false;
    }

    public TestRunManager(BrowserTestRunner testRunner) {
        this(testRunner, null);
    }

    public TestRunManager(BrowserTestRunner testRunner, String overrideUrl) {
        this.testRunner = testRunner;
        this.overrideUrl = overrideUrl;
        browserFileNames = testRunner.getBrowserFileNames();
    }

    public void runTests() {
        initializeTestRunResult();
        testRunner.logStatus("Starting Test Run");
        testRunner.startTestRun();
        try {
            for (String browserFileName : browserFileNames) {
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

    public void limitToBrowserWithId(int browserId) throws InvalidBrowserIdException {
        String browserFileName = testRunner.getBrowserFileNameById(browserId);
        if (browserFileName == null)
            throw new InvalidBrowserIdException(browserId);
        browserFileNames = Arrays.asList(new String[]{browserFileName});
    }
}