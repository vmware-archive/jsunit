package net.jsunit.interceptor;

import net.jsunit.BrowserTestRunner;
import net.jsunit.JsUnitServer;

public class DefaultBrowserTestRunnerSource implements BrowserTestRunnerSource {

	public BrowserTestRunner getRunner() {
		return JsUnitServer.instance();
	}

}
