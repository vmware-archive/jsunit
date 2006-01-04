package net.jsunit;
 
import junit.framework.TestCase;

import java.util.Date;

import net.jsunit.model.BrowserResult;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class StandaloneTest extends TestCase {
    private boolean shouldStopServer = false;
    public static final int MAX_SECONDS_TO_WAIT = 60;
    private JsUnitServer server;
	private int errorCount;
	private int failureCount;

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
    }

    public void tearDown() throws Exception {
        if (shouldStopServer)
            server.stop();
        super.tearDown();
    }

    public void testStandaloneRun() throws Exception {
        for (String browserFileName : server.getBrowserFileNames()) {
            Date dateLaunched = new Date();
        	server.launchTestRunForBrowserWithFileName(browserFileName);
            waitForResultToBeSubmitted(browserFileName, dateLaunched);
            verifyLastResult();
        }
        if (errorCount>0 || failureCount>0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("The Test Run had problems: ");
            buffer.append(errorCount).append(" errors, ");
            buffer.append(failureCount).append(" failures ");
            fail(buffer.toString());
        }
    }

    private void waitForResultToBeSubmitted(String browserFileName, Date dateBrowserLaunched) throws Exception {
        Utility.log("StandaloneTest: waiting for " + browserFileName + " to submit result");
        long secondsWaited = 0;
        while (!server.hasReceivedResultSince(dateBrowserLaunched)) {
            Thread.sleep(1000);
            secondsWaited++;
            if (secondsWaited > MAX_SECONDS_TO_WAIT)
                fail("Waited more than " + MAX_SECONDS_TO_WAIT + " seconds for browser " + browserFileName);
        }
    }

    private void verifyLastResult() {
        BrowserResult result = server.lastResult();
        if (!result.wasSuccessful()) {
        	errorCount += result.errorCount();
        	failureCount += result.errorCount();
        }
    }

	public JsUnitServer getJsUnitServer() {
		return server;
	}

}