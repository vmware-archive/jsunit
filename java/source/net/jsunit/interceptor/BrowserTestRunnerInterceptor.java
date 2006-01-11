package net.jsunit.interceptor;

import net.jsunit.BrowserTestRunner;
import net.jsunit.action.JsUnitAction;

import com.opensymphony.xwork.Action;

public class BrowserTestRunnerInterceptor extends InterceptorSupport {

	private static BrowserTestRunnerSource source = new DefaultBrowserTestRunnerSource();

	public static void setBrowserTestRunnerSource(BrowserTestRunnerSource aSource) {
		source = aSource;
	}

	protected void execute(Action action) {
		JsUnitAction jsUnitAction = ((JsUnitAction) action);
		BrowserTestRunner runner = source.getRunner();
		jsUnitAction.setBrowserTestRunner(runner);
	}

}
