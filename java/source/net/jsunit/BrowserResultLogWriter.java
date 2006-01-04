package net.jsunit;
 
import java.io.File;

import net.jsunit.model.BrowserResult;

public class BrowserResultLogWriter implements BrowserTestRunListener {

	private File logsDirectory;
	
	public BrowserResultLogWriter(File logsDirectory) {
		this.logsDirectory = logsDirectory;
	}

	public void browserTestRunFinished(String browserFileName, BrowserResult result) {
		result.writeLog(logsDirectory);
	}

	public void browserTestRunStarted(String browserFileName) {
	}

}