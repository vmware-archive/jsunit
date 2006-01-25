package net.jsunit;
 
import net.jsunit.model.BrowserResult;

public interface TestRunListener {
	
	boolean isReady();

	void testRunStarted();

	void testRunFinished();

	void browserTestRunStarted(String browserFileName);
	
	void browserTestRunFinished(String browserFileName, BrowserResult result);

}