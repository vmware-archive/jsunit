package net.jsunit;

import junit.framework.TestCase;

import java.util.Iterator;
import java.util.Date;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class StandaloneTest extends TestCase {
    private boolean needToStopServer = false;
    public static final int MAX_SECONDS_TO_WAIT = 2 * 60;
    private JsUnitServer server;
    private Process process;

  public static final String DEFAULT_SYSTEM_BROWSER = "htmlview";

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
            server.initialize();
            server.start();
            needToStopServer = true;
        }
    }

    public void tearDown() throws Exception {
        if (needToStopServer)
            server.stop();
        if (process != null) {
            destroyBrowserProcess();

        }
        super.tearDown();
    }

    public void testStandaloneRun() throws Exception {
        Iterator it = server.getLocalBrowserFileNames().iterator();
        while (it.hasNext()) {
            String browserFileName = (String) it.next();
            Date dateLaunched = new Date();
            launchBrowser(browserFileName);
            waitForResultToBeSubmitted(browserFileName, dateLaunched);
            destroyBrowserProcess();
            verifyLastResult();
        }
    }

    private void destroyBrowserProcess() {
        process.destroy();
        process = null;
    }

    private void launchBrowser(String browserFileName) {
        Utility.log("StandaloneTest: launching " + browserDisplayName(browserFileName));
        try {
            process = Runtime.getRuntime().exec(new String[] {browserFileName, server.getTestURL().toString()});
        } catch (Throwable t) {
            t.printStackTrace();
            fail("All browser processes should start, but the following did not: " + browserDisplayName(browserFileName));
        }
    }

  private String browserDisplayName(String browserFileName) {
    if (browserFileName.equals(DEFAULT_SYSTEM_BROWSER))
        return "<Default System Browser>";
    return browserFileName;
  }

  private void waitForResultToBeSubmitted(String browserFileName, Date dateBrowserLaunched) throws Exception {
        Utility.log("StandaloneTest: waiting for " + browserDisplayName(browserFileName) + " to submit result");
        long secondsWaited = 0;
        while (!server.hasReceivedResultSince(dateBrowserLaunched)) {
            Thread.sleep(1000);
            secondsWaited ++;
            if (secondsWaited > MAX_SECONDS_TO_WAIT)
                fail("Waited more than " + MAX_SECONDS_TO_WAIT + " seconds for browser " + browserDisplayName(browserFileName));
        }
    }

    private void verifyLastResult() {
        TestSuiteResult result = server.lastResult();
        if (!result.hadSuccess()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Result with ID ");
            buffer.append(result.getId());
            buffer.append(" had problems: ");
            buffer.append(result.errorCount() + " errors, ");
            buffer.append(result.failureCount() + " failures ");
            fail(buffer.toString());
        }
    }

}