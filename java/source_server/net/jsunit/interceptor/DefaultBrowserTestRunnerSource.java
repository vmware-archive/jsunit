package net.jsunit.interceptor;

import net.jsunit.BrowserTestRunner;
import net.jsunit.StandardJsUnitServer;

public class DefaultBrowserTestRunnerSource implements BrowserTestRunnerSource {

	public BrowserTestRunner getRunner() {
		return StandardJsUnitServer.serverInstance();
	}

}
