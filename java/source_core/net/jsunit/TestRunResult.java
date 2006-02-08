package net.jsunit;

import net.jsunit.model.BrowserResult;

import java.util.ArrayList;
import java.util.List;

public class TestRunResult {
    private List<BrowserResult> browserResults = new ArrayList<BrowserResult>();

    public int getFailureCount() {
        int count = 0;
        for (BrowserResult result : browserResults)
            count += result.failureCount();
        return count;
    }

    public int getErrorCount() {
        int count = 0;
        for (BrowserResult result : browserResults)
            count += result.errorCount();
        return count;
    }

    public boolean wasSuccessful() {
        for (BrowserResult result : browserResults)
            if (result.failureCount()>0 || result.errorCount() >0)
                return false;
        return true;
    }

    public void addBrowserResult(BrowserResult browserResult) {
        browserResults.add(browserResult);
    }
}
