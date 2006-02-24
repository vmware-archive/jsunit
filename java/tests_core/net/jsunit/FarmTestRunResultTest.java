package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.FarmTestRunResult;
import net.jsunit.model.ResultType;
import net.jsunit.model.TestRunResult;
import net.jsunit.utility.XmlUtility;

import java.net.URL;

public class FarmTestRunResultTest extends TestCase {

    public void testSimple() throws Exception {
        FarmTestRunResult farmResult = new FarmTestRunResult();

        TestRunResult result1 = new TestRunResult();
        result1.addBrowserResult(successResult());
        result1.addBrowserResult(successResult());
        farmResult.addTestRunResult(result1);

        assertEquals(ResultType.SUCCESS, farmResult.getResultType());
        assertTrue(farmResult.wasSuccessful());

        TestRunResult result2 = new TestRunResult();
        result2.addBrowserResult(failureResult());
        result2.addBrowserResult(errorResult());
        farmResult.addTestRunResult(result2);

        assertEquals(ResultType.ERROR, farmResult.getResultType());
        assertFalse(farmResult.wasSuccessful());
        assertEquals(1, farmResult.getFailureCount());
        assertEquals(1, farmResult.getErrorCount());
    }

    public void testUnresponsiveRemoteURL() throws Exception {
        FarmTestRunResult farmResult = new FarmTestRunResult();

        TestRunResult result1 = new TestRunResult();
        result1.addBrowserResult(successResult());
        result1.addBrowserResult(successResult());
        farmResult.addTestRunResult(result1);

        TestRunResult result2 = new TestRunResult(new URL("http://my.domain.com:8201"));
        result2.setUnresponsive();
        farmResult.addTestRunResult(result2);

        TestRunResult result3 = new TestRunResult(new URL("http://my.domain.com:8201"));
        result3.setUnresponsive();
        farmResult.addTestRunResult(result3);

        assertEquals(ResultType.UNRESPONSIVE, farmResult.getResultType());
    }

    public void testAsXml() throws Exception {
        FarmTestRunResult farmResult = new FarmTestRunResult();

        TestRunResult result1 = new TestRunResult();
        result1.addBrowserResult(successResult());
        result1.addBrowserResult(successResult());
        farmResult.addTestRunResult(result1);

        TestRunResult result2 = new TestRunResult();
        result2.addBrowserResult(failureResult());
        result2.addBrowserResult(errorResult());
        farmResult.addTestRunResult(result2);

        TestRunResult result3 = new TestRunResult(new URL("http://my.domain.com:4732"));
        result3.setUnresponsive();
        farmResult.addTestRunResult(result3);

        assertEquals(
                "<farmTestRunResult type=\"UNRESPONSIVE\">" +
                    XmlUtility.asString(result1.asXml()) +
                    XmlUtility.asString(result2.asXml()) +
                    "<testRunResult type=\"UNRESPONSIVE\" url=\"http://my.domain.com:4732\" />" +
                "</farmTestRunResult>",
                XmlUtility.asString(farmResult.asXml())
        );
    }

    private BrowserResult successResult() {
        return new BrowserResult();
    }

    private BrowserResult failureResult() {
    	return new DummyBrowserResult(false, 1, 0);
    }

    private BrowserResult errorResult() {
        return new DummyBrowserResult(false, 0, 1);
    }

}
