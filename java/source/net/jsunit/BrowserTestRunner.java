package net.jsunit;

import java.util.Date;
import java.util.List;

import net.jsunit.model.BrowserResult;

public interface BrowserTestRunner {

	List<String> getBrowserFileNames();

	void launchTestRunForBrowserWithFileName(String browserFileName) throws FailedToLaunchBrowserException;

	boolean hasReceivedResultSince(Date dateBrowserLaunched);

	BrowserResult lastResult();

	void accept(BrowserResult result);

	void dispose();

	BrowserResult findResultWithId(String id);

}
