package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.BrowserTestRunner;
import net.jsunit.action.BrowserTestRunnerAware;

public class BrowserTestRunnerInterceptor extends JsUnitInterceptor {

    private static BrowserTestRunnerSource source = new DefaultBrowserTestRunnerSource();

    public static void setBrowserTestRunnerSource(BrowserTestRunnerSource aSource) {
        source = aSource;
    }

    protected void execute(Action action) throws Exception {
        if (action instanceof BrowserTestRunnerAware) {
            BrowserTestRunnerAware aware = (BrowserTestRunnerAware) action;
            BrowserTestRunner runner = source.getRunner();
            aware.setBrowserTestRunner(runner);
        }
    }

}
