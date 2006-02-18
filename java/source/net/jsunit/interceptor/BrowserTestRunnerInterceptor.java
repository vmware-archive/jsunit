package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.BrowserTestRunner;
import net.jsunit.action.JsUnitAction;

public class BrowserTestRunnerInterceptor extends JsUnitInterceptor {

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
