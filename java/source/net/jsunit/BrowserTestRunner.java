package net.jsunit;

import net.jsunit.model.BrowserResult;

import java.util.List;

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
