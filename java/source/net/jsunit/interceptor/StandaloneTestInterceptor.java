package net.jsunit.interceptor;

import net.jsunit.StandaloneTest;
import net.jsunit.action.StandaloneTestAware;

import com.opensymphony.xwork.Action;

public class StandaloneTestInterceptor extends InterceptorSupport {

	protected void execute(Action targetAction) {
		StandaloneTestAware aware = (StandaloneTestAware) targetAction;
		StandaloneTest standaloneTest = new StandaloneTest("testStandaloneRun");
		aware.setStandaloneTest(standaloneTest);
	}

}
