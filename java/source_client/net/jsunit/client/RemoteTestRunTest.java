package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.PlatformType;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserType;
import net.jsunit.model.DistributedTestRunResult;

class RemoteTestRunTest extends TestCase {
    private boolean done;
    private PlatformType platformType;
    private BrowserType browserType;
    private Exception error;

    public RemoteTestRunTest(PlatformType platformType, BrowserType browserType) {
        super(platformType.getDisplayName() + "/" + browserType.getDisplayName());
        this.platformType = platformType;
        this.browserType = browserType;
    }

    public int countTestCases() {
        return 1;
    }

    protected void runTest() throws Throwable {
        while (!done)
            Thread.sleep(100);
        if (error != null)
            throw error;
    }

    public void notifyResult(DistributedTestRunResult distributedResult) {
        BrowserResult browserResult = distributedResult.findBrowserResultMatching(platformType, browserType);
        if (browserResult == null)
            error = new NoSuchPlatformBrowserException();
        else if (!browserResult.wasSuccessful()) {
            StringBuffer buffer = new StringBuffer();
            browserResult.addErrorStringTo(buffer);
            error = new RemoteTestFailedException(buffer.toString());
        }
        done = true;
    }

    public void notifyError(Exception e) {
        error = e;
        done = true;
    }

}
