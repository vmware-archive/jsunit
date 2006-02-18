package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;

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
        	JsUnitServer server = new JsUnitServer(new Configuration(configurationSource()));
        	server.start();
            runner = server;
            shouldDisposeOfRunner = true;
        }
        testRunManager = createTestRunManager();
    }

	protected ConfigurationSource configurationSource() {
		return Configuration.resolveSource();
	}

	protected TestRunManager createTestRunManager() {
		return new TestRunManager(runner);
	}

    public void tearDown() throws Exception {
        if (shouldDisposeOfRunner)
            runner.dispose();
        super.tearDown();
    }

    public void testStandaloneRun() throws Exception {
    	testRunManager.runTests();
    	if (testRunManager.hadProblems()) {
    		fail(Utility.asPrettyString(testRunManager.getTestRunResult().asXml()));
    		
    	}
    }

	public BrowserTestRunner getBrowserTestRunner() {
		return runner;
	}

}