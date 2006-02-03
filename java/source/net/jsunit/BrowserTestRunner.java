package net.jsunit;

import java.util.Date;
import java.util.List;

import net.jsunit.model.BrowserResult;

public interface BrowserTestRunner extends XmlRenderable {

	void startTestRun();

	void finishTestRun();

	void launchTestRunForBrowserWithFileName(String browserFileName);

    void accept(BrowserResult result);

    boolean hasReceivedResultSince(Date dateBrowserLaunched);

    BrowserResult lastResult();

    void dispose();

    BrowserResult findResultWithId(String id);
	
	void logStatus(String message);

	List<String> getBrowserFileNames();

}
