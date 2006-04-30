package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;

import java.util.List;

public interface BrowserTestRunner extends XmlRenderable, SkinSource {

    void startTestRun();

    void finishTestRun();

    void launchBrowserTestRun(BrowserLaunchSpecification launchSpec);

    void accept(BrowserResult result);

    boolean isWaitingForBrowser(Browser browser);

    void addTestRunListener(TestRunListener listener);

    void removeTestRunListener(TestRunListener listener);

    void dispose();

    BrowserResult findResultWithId(String id, int browserId) throws InvalidBrowserIdException;

    void logStatus(String message);

    List<Browser> getBrowsers();

    int timeoutSeconds();

    boolean isAlive();

    Configuration getConfiguration();

    String getSecretKey();
}
