package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class StandaloneTest extends TestCase {
    private boolean shouldDisposeOfRunner = false;
    protected BrowserTestRunner runner;
	private TestRunManager testRunManager;

    public StandaloneTest(String name) {
    	super(name);
	}

	public void setBrowserTestRunner(BrowserTestRunner runner) {
        this.runner = runner;
    }

    public void setUp() throws Exception {
        super.setUp();
        if (runner == null) {
        	JsUnitServer server = new JsUnitServer(Configuration.resolve());
        	server.start();
            runner = server;
            shouldDisposeOfRunner = true;
        }
        testRunManager = new TestRunManager(runner);
    }

    public void tearDown() throws Exception {
        if (shouldDisposeOfRunner)
            runner.dispose();
        super.tearDown();
    }

    public void testStandaloneRun() throws Exception {
    	testRunManager.runTests();
    	if (testRunManager.hadProblems()) {
    		System.out.println(Utility.asPrettyString(testRunManager.getTestRunResult().asXml()));
    		fail();
    	}
    }

	public BrowserTestRunner getBrowserTestRunner() {
		return runner;
	}

}