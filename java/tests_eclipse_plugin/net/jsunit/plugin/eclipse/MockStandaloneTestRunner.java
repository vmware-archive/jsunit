package net.jsunit.plugin.eclipse;

import net.jsunit.StandaloneTest;
import net.jsunit.model.BrowserResult;

public class MockStandaloneTestRunner implements StandaloneTestRunner {

	public StandaloneTest test;

	public void runStandaloneTest(StandaloneTest test) {
		this.test = test;
		this.test.getBrowserTestRunner().accept(new BrowserResult());
	}

}
