package net.jsunit;
 
import junit.framework.TestCase;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class StandaloneTest extends TestCase {
    private boolean shouldStopServer = false;
    private JsUnitServer server;
	private TestRunManager testRunManager;

    public StandaloneTest(String name) {
    	super(name);
	}

	public void setServer(JsUnitServer server) {
        this.server = server;
    }

    public void setUp() throws Exception {
        super.setUp();
        if (server == null) {
            server = new JsUnitServer();
            server.start();
            shouldStopServer = true;
        }
        testRunManager = new TestRunManager(server);
    }

    public void tearDown() throws Exception {
        if (shouldStopServer)
            server.stop();
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

	public JsUnitServer getJsUnitServer() {
		return server;
	}

}