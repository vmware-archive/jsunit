package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.BrowserTestRunner;
import net.jsunit.action.JsUnitServerAction;

public class BrowserTestRunnerInterceptor extends JsUnitInterceptor {

	private static BrowserTestRunnerSource source = new DefaultBrowserTestRunnerSource();

	public static void setBrowserTestRunnerSource(BrowserTestRunnerSource aSource) {
		source = aSource;
	}

	protected void execute(Action action) {
		JsUnitServerAction jsUnitAction = ((JsUnitServerAction) action);
		BrowserTestRunner runner = source.getRunner();
		jsUnitAction.setBrowserTestRunner(runner);
	}

}
