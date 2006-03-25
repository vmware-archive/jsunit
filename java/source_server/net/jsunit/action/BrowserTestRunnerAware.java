package net.jsunit.action;

import net.jsunit.BrowserTestRunner;

public interface BrowserTestRunnerAware {

    void setBrowserTestRunner(BrowserTestRunner runner);

    BrowserTestRunner getBrowserTestRunner();
}
