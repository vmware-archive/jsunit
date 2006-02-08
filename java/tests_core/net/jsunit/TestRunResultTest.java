package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.model.BrowserResult;

public class TestRunResultTest extends TestCase {
    private TestRunResult testRunResult;

    protected void setUp() throws Exception {
        super.setUp();
        testRunResult = new TestRunResult();
    }

    public void testSuccess() throws Exception {
        testRunResult.addBrowserResult(successResult());
        testRunResult.addBrowserResult(successResult());
        assertTrue(testRunResult.wasSuccessful());
        assertEquals(0, testRunResult.getErrorCount());
        assertEquals(0, testRunResult.getFailureCount());
    }

    public void testFailuresAndErrors() throws Exception {
        testRunResult.addBrowserResult(failureResult());
        assertFalse(testRunResult.wasSuccessful());
        assertEquals(0, testRunResult.getErrorCount());
        assertEquals(1, testRunResult.getFailureCount());

        testRunResult.addBrowserResult(failureResult());
        assertFalse(testRunResult.wasSuccessful());
        assertEquals(0, testRunResult.getErrorCount());
        assertEquals(2, testRunResult.getFailureCount());

        testRunResult.addBrowserResult(errorResult());
        assertFalse(testRunResult.wasSuccessful());
        assertEquals(1, testRunResult.getErrorCount());
        assertEquals(2, testRunResult.getFailureCount());

    }

    private BrowserResult successResult() {
        return new BrowserResult();
    }

    private BrowserResult failureResult() {
        return new BrowserResult() {
            public int failureCount() {
                return 1;
            }
        };
    }

    private BrowserResult errorResult() {
        return new BrowserResult() {
            public int errorCount() {
                return 1;
            }
        };
    }

}
