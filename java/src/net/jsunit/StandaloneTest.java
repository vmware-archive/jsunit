package net.jsunit;

import junit.framework.TestCase;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class StandaloneTest extends TestCase {
    private boolean shouldStartAndStopServer = true;
    public static final String PROPERTY_URL = "url";
    public static final String PROPERTY_BROWSER_FILE_NAMES = "browserFileNames";
    public static final int MAX_SECONDS_TO_WAIT = 2 * 60;
    private JsUnitServer server = JsUnitServer.instance();
    private Properties properties;

    public StandaloneTest(String name) {
        super(name);
    }

    protected List browserFileNames() {
        return Utility.listFromCommaDelimitedString(properties.getProperty(PROPERTY_BROWSER_FILE_NAMES));
    }

    protected String url() {
        return properties.getProperty(PROPERTY_URL);
    }

    public void setUp() throws Exception {
        super.setUp();
        this.properties = server.getJsUnitProperties();
        if (shouldStartAndStopServer)
            server.start();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        if (shouldStartAndStopServer)
            server.stop();
    }

    public void testStandaloneRun() throws Exception {
        Iterator it = browserFileNames().iterator();
        while (it.hasNext()) {
            String next = (String) it.next();
            int currentResultCount = server.resultsCount();
            Process process = null;
            Utility.log("StandaloneTest: launching " + next);
            try {
                try {
                    process =
                            Runtime.getRuntime().exec("\"" + next + "\" \"" + url() + "\"");
                } catch (Throwable t) {
                    t.printStackTrace();
                    fail("All browser processes should start, but the following did not: "
                            + next);
                }
                waitForResultToBeSubmitted(next, currentResultCount);
                verifyLastResult();
            } finally {
                if (process != null)
                    process.destroy();
            }
        }
    }

    private void waitForResultToBeSubmitted(String browserProcess,
                                            int initialResultCount)
            throws Exception {
        Utility.log("StandaloneTest: waiting for "
                + browserProcess
                + " to submit result");
        long secondsWaited = 0;
        while (server.getResults().size() == initialResultCount) {
            Thread.sleep(1000);
            secondsWaited += 1;
            if (secondsWaited > MAX_SECONDS_TO_WAIT)
                fail("Waited more than "
                        + MAX_SECONDS_TO_WAIT
                        + " seconds for browser "
                        + browserProcess);
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

    public void setStartAndStopServer(boolean b) {
        this.shouldStartAndStopServer = b;
    }
}
