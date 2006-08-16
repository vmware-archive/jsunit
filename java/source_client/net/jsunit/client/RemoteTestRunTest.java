package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserSpecification;
import net.jsunit.model.DistributedTestRunResult;

class RemoteTestRunTest extends TestCase {
    private boolean done;
    private Exception error;
    private BrowserSpecification browserSpec;

    public RemoteTestRunTest(BrowserSpecification browserSpec) {
        super(browserSpec.displayString());
        this.browserSpec = browserSpec;
    }

    protected void runTest() throws Throwable {
        while (!done)
            Thread.sleep(100);
        if (error != null)
            throw error;
    }

    public void notifyResult(DistributedTestRunResult distributedResult) {
        BrowserResult browserResult = distributedResult.findBrowserResultMatching(browserSpec);
        if (browserResult == null)
            error = new NoSuchPlatformBrowserException(browserSpec);
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
