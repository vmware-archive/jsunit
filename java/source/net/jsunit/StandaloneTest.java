package net.jsunit;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class StandaloneTest extends TestCase {
    private boolean needToStopServer = false;
    public static final int MAX_SECONDS_TO_WAIT = 2 * 60;
    private JsUnitServer server;
    private Process process;

    public static final String DEFAULT_SYSTEM_BROWSER = "default";

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
        for (String browserFileName : server.getLocalBrowserFileNames()) {
            String[] browserCommand = determineOpenBrowserCommand(browserFileName);
            Date dateLaunched = new Date();
            launchBrowser(browserCommand);
            waitForResultToBeSubmitted(browserFileName, dateLaunched);
            destroyBrowserProcess();
            verifyLastResult();
        }
    }

    private String[] determineOpenBrowserCommand(String browserFileName) {
        if (browserFileName.equals(DEFAULT_SYSTEM_BROWSER)) {
            if (isWindows()) {
                return new String[] {"rundll32", "url.dll,FileProtocolHandler"};
            }
            else return new String[] {"htmlview"};
        }
        return new String[] {browserFileName};
    }

    private void destroyBrowserProcess() {
        process.destroy();
        process = null;
    }

    private void launchBrowser(String[] browserCommand) {
        Utility.log("StandaloneTest: launching " + browserCommand[0]);
        try {
            String[] commandWithUrl = new String[browserCommand.length + 1];
            System.arraycopy(browserCommand, 0, commandWithUrl, 0, browserCommand.length);
            commandWithUrl[browserCommand.length] = server.getTestURL().toString();
            process = Runtime.getRuntime().exec(commandWithUrl);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("All browser processes should start, but the following did not: " + browserCommand[0]);
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
        TestSuiteResult result = server.lastResult();
        if (!result.hadSuccess()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Result with ID ");
            buffer.append(result.getId());
            buffer.append(" had problems: ");
            buffer.append(result.errorCount()).append(" errors, ");
            buffer.append(result.failureCount()).append(" failures ");
            fail(buffer.toString());
        }
    }

    private boolean isWindows() {
        String os = System.getProperty("os.name");
        return os != null && os.startsWith("Windows");
    }

}