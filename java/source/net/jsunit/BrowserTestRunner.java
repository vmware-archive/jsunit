package net.jsunit;

import java.util.List;

import net.jsunit.model.BrowserResult;

public interface BrowserTestRunner extends XmlRenderable {

	void startTestRun();

	void finishTestRun();

	long launchTestRunForBrowserWithFileName(String browserFileName);

    void accept(BrowserResult result);

    boolean hasReceivedResultSince(long launchTime);

    BrowserResult lastResult();

    void dispose();

    BrowserResult findResultWithId(String id);
	
	void logStatus(String message);

	List<String> getBrowserFileNames();

	int timeoutSeconds();

}
