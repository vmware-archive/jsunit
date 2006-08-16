package net.jsunit;

import net.jsunit.model.TestRunResult;
import net.jsunit.model.DummyBrowserResult;

public class DummyFailedTestRunResult extends TestRunResult {

    public DummyFailedTestRunResult() {
        addBrowserResult(new DummyBrowserResult(false, 1, 2));
        addBrowserResult(new DummyBrowserResult(false, 1, 2));
    }
}
