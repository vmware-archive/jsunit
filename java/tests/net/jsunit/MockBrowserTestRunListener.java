package net.jsunit;

import net.jsunit.model.BrowserResult;

public class MockBrowserTestRunListener implements BrowserTestRunListener {

	public boolean testRunFinishedCalled;
	public boolean testRunStartedCalled;

	public void browserTestRunFinished(String browserFileName, BrowserResult result) {
		testRunFinishedCalled = true;
	}

	public void browserTestRunStarted(String browserFileName) {
		testRunStartedCalled = true;
	}

}