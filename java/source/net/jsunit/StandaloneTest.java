package net.jsunit;
 
import net.jsunit.configuration.Configuration;
import junit.framework.TestCase;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class StandaloneTest extends TestCase {
    private boolean shouldDisposeOfRunner = false;
    private BrowserTestRunner runner;
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
            StringBuffer buffer = new StringBuffer();
            buffer.append("The Test Run had problems: ");
            buffer.append(testRunManager.getErrorCount()).append(" errors, ");
            buffer.append(testRunManager.getFailureCount()).append(" failures ");
            fail(buffer.toString());    		
    	}
    }

	public BrowserTestRunner getBrowserTestRunner() {
		return runner;
	}

}