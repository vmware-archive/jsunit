package net.jsunit;

import net.jsunit.model.BrowserResult;

import java.io.File;

public class BrowserResultLogWriter implements TestRunListener {

	private File logsDirectory;
	
	public BrowserResultLogWriter(File logsDirectory) {
		this.logsDirectory = logsDirectory;
	}

	public void browserTestRunFinished(String browserFileName, BrowserResult result) {
		result.writeLog(logsDirectory);
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