package net.jsunit.plugin.eclipse;

import junit.textui.TestRunner;
import net.jsunit.StandaloneTest;

public class DefaultStandaloneTestRunner implements StandaloneTestRunner {

	public void runStandaloneTest(StandaloneTest test) {
		TestRunner.run(test);
	}

}