package net.jsunit;
 
import net.jsunit.model.BrowserResult;

public interface BrowserTestRunListener {
	
	void browserTestRunStarted(String browserFileName);
	
	void browserTestRunFinished(String browserFileName, BrowserResult result);

}