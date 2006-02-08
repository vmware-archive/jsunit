package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.StandaloneTest;
import net.jsunit.action.StandaloneTestAware;

public class StandaloneTestInterceptor extends InterceptorSupport {

	protected void execute(Action targetAction) {
		StandaloneTestAware aware = (StandaloneTestAware) targetAction;
		StandaloneTest standaloneTest = new StandaloneTest("testStandaloneRun");
		aware.setStandaloneTest(standaloneTest);
	}

}
