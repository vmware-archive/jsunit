package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockBrowserTestRunner extends BrowserTestRunnerStub {

    public boolean disposeCalled;
    public BrowserResult acceptedResult;
    public BrowserResult resultToReturn;
    public boolean shouldSucceed;
    public String idPassed;
    public Integer browserIdPassed;
    public int timeoutSeconds;
    public List<String> logMessages = new ArrayList<String>();
    public List<BrowserLaunchSpecification> launchSpecs = new ArrayList<BrowserLaunchSpecification>();
    public Configuration configuration;
    private TestRunListener testRunManager;
    public boolean waitingForBrowser;

    public void launchBrowserTestRun(BrowserLaunchSpecification launchSpec) {
        launchSpecs.add(launchSpec);
        BrowserResult result = createResult();
        Browser browser = launchSpec.getBrowser();
        result.setBrowser(browser);
        accept(result);
    }

    public void addTestRunListener(TestRunListener listener) {
        if (listener instanceof TestRunManager)
            this.testRunManager = listener;
    }

    public void removeTestRunListener(TestRunListener listener) {
        if (listener instanceof TestRunManager)
            this.testRunManager = null;
    }

    public boolean isWaitingForBrowser(Browser browser) {
        return waitingForBrowser;
    }

    public void accept(BrowserResult result) {
        this.acceptedResult = result;
        if (testRunManager != null)
            testRunManager.browserTestRunFinished(result.getBrowser(), result);
    }

    public BrowserResult lastResult() {
        return createResult();
    }

    private DummyBrowserResult createResult() {
        return new DummyBrowserResult(shouldSucceed, shouldSucceed ? 0 : 1, 0);
    }

    public void dispose() {
        disposeCalled = true;
    }

    public BrowserResult findResultWithId(String id, int browserId) throws InvalidBrowserIdException {
        idPassed = id;
        browserIdPassed = browserId;
        return resultToReturn;
    }

    public void logStatus(String message) {
        logMessages.add(message);
    }

    public List<Browser> getBrowsers() {
        return Arrays.asList(new Browser[]{new Browser("mybrowser1.exe", 0), new Browser("mybrowser2.exe", 1)});
    }

    public int timeoutSeconds() {
        return timeoutSeconds;
    }

    public boolean isAlive() {
        return true;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
