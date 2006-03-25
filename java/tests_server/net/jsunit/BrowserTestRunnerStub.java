package net.jsunit;

import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.results.Skin;
import org.jdom.Element;

import java.util.List;

public class BrowserTestRunnerStub implements BrowserTestRunner {
    public void startTestRun() {
    }

    public void finishTestRun() {
    }

    public long launchBrowserTestRun(BrowserLaunchSpecification launchSpec) {
        return 0;
    }

    public void accept(BrowserResult result) {
    }

    public boolean hasReceivedResultSince(long launchTime) {
        return false;
    }

    public BrowserResult lastResult() {
        return null;
    }

    public void dispose() {
    }

    public BrowserResult findResultWithId(String id, int browserId) throws InvalidBrowserIdException {
        return null;
    }

    public void logStatus(String message) {
    }

    public List<Browser> getBrowsers() {
        return null;
    }

    public int timeoutSeconds() {
        return 0;
    }

    public boolean isAlive() {
        return false;
    }

    public Element asXml() {
        return null;
    }

    public List<Skin> getSkins() {
        return null;
    }

    public Skin getSkinById(int skinId) {
        return null;
    }
}
