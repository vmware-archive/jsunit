package net.jsunit;

import net.jsunit.model.BrowserResult;

public class MockTestRunListener implements TestRunListener {

	public boolean testRunStartedCalled;
	public boolean testRunFinishedCalled;
	public boolean browserTestRunStartedCalled;
	public boolean browserTestRunFinishedCalled;
	public String browserFileName;
	public boolean isReady;
	public BrowserResult result;

	public void browserTestRunFinished(String browserFileName, BrowserResult result) {
		browserTestRunFinishedCalled = true;
		this.browserFileName = browserFileName;
		this.result = result;
	}

	public void browserTestRunStarted(String browserFileName) {
		browserTestRunStartedCalled = true;
		this.browserFileName = browserFileName;
	}
	
	public boolean isReady() {
		return isReady;
	}

	public void testRunStarted() {
		testRunStartedCalled = true;
	}

	public void testRunFinished() {
		testRunFinishedCalled = true;
	}

}