package net.jsunit;

import net.jsunit.logging.BrowserResultRepository;
import net.jsunit.model.BrowserResult;

public class BrowserResultLogWriter implements TestRunListener {

    private BrowserResultRepository repository;

    public BrowserResultLogWriter(BrowserResultRepository repository) {
        this.repository = repository;
    }

    public void browserTestRunFinished(String browserFileName, BrowserResult result) {
        repository.store(result);
    }

    public void browserTestRunStarted(String browserFileName) {
    }

    public boolean isReady() {
        return true;
    }

    public void testRunStarted() {
    }

    public void testRunFinished() {
    }

}