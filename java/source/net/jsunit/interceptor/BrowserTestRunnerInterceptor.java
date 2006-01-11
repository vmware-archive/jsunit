package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;

import net.jsunit.BrowserTestRunner;
import net.jsunit.action.JsUnitAction;

public class BrowserTestRunnerInterceptor extends InterceptorSupport {

	private static BrowserTestRunner runner;

	public static void setBrowserTestRunner(BrowserTestRunner aRunner) {
		runner = aRunner;
	}

	protected void execute(Action action) {
		((JsUnitAction) action).setBrowserTestRunner(runner);
	}

}
