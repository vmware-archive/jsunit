package net.jsunit;

import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserLaunchSpecification;
import net.jsunit.model.BrowserResult;
import org.jdom.Element;

import java.util.List;

public class BrowserTestRunnerStub implements BrowserTestRunner {
    public void startTestRun() {
    }

    public void finishTestRun() {
    }

    public void launchBrowserTestRun(BrowserLaunchSpecification launchSpec) {
    }

    public void accept(BrowserResult result) {
    }

    public boolean isWaitingForBrowser(Browser browser) {
        return false;
    }

    public void addTestRunListener(TestRunListener listener) {
    }

    public void removeTestRunListener(TestRunListener listener) {
    }

    public void dispose() {
    }

    public BrowserResult findResultWithId(String id, int browserId) throws InvalidBrowserIdException {
        return null;
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

    public ServerConfiguration getConfiguration() {
        return null;
    }

    public String getSecretKey() {
        return null;
    }

    public Element asXml() {
        return null;
    }

}
