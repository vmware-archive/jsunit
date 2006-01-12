package net.jsunit.model;

import junit.framework.TestCase;

import java.io.File;

import net.jsunit.Utility;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.TestCaseResult;
import org.jdom.Element;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class BrowserResultTest extends TestCase {
    private BrowserResult result;
    private String expectedXmlFragment =
            "<testrun id=\"An ID\" errors=\"1\" failures=\"1\" name=\"JsUnitTestCase\" tests=\"3\" time=\"4.3\">"
                + "<properties>"
                    + "<property name=\"JsUnitVersion\" value=\"2.5\" />"
                    + "<property name=\"userAgent\" value=\"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)\" />"
                    + "<property name=\"remoteAddress\" value=\"Dummy Remote Address\" />"
                    + "<property name=\"baseURL\" value=\"http://www.example.com/\" />"
                + "</properties>"
                    + "<testcase name=\"page.html:testFoo\" time=\"1.3\" />"
                    + "<testcase name=\"page.html:testFoo\" time=\"1.3\">"
                        + "<error message=\"Test Error Message\" />"
                    + "</testcase>"
                    + "<testcase name=\"page.html:testFoo\" time=\"1.3\">"
                        + "<failure message=\"Test Failure Message\" />"
                    + "</testcase>"
            + "</testrun>";

    public void setUp() throws Exception {
        super.setUp();
        result = createBrowserResult();
        result.setTestCaseStrings(new String[] {
            "page.html:testFoo|1.3|S||",
            "page.html:testFoo|1.3|E|Test Error Message|",
            "page.html:testFoo|1.3|F|Test Failure Message|"}
        );
    }

    public void testId() {
        assertNotNull(result.getId());
        result = new BrowserResult();
        result.setId("foo");
        assertEquals("foo", result.getId());
    }

    public void testFields() {
        assertFields(result);
    }

    public void testXml() {
        assertEquals(expectedXmlFragment, result.asXmlFragment());
    }

    public void testResultType() {
        assertFalse(result.wasSuccessful());
        assertEquals(ResultType.ERROR, result.getResultType());
    }

    public void testBuildFromXmlFile() {
        Element element = result.asXml();
        String xmlString = Utility.asString(element);
        Utility.writeFile(xmlString, "resultXml.xml");
        File file = new File("resultXml.xml");
        BrowserResult reconstitutedResult = BrowserResult.fromXmlFile(file);
        assertFields(reconstitutedResult);
        file.delete();
    }

    public void testFailure() {
        BrowserResult result = createBrowserResult();
        result.setTestCaseStrings(new String[] {
            "page.html:testFoo|1.3|S||",
            "page.html:testBar|1.3|F|Test Failure Message|"
        });
        assertFalse(result.wasSuccessful());
        assertEquals(ResultType.FAILURE, result.getResultType());
    }

    public void testSuccess() {
        BrowserResult result = createBrowserResult();
        result.setTestCaseStrings(new String[] {
            "page.html:testFoo|1.3|S||",
            "page.html:testBar|1.3|S||"
        });
        assertTrue(result.wasSuccessful());
        assertEquals(ResultType.SUCCESS, result.getResultType());
    }

    private BrowserResult createBrowserResult() {
        BrowserResult browserResult = new BrowserResult();
        browserResult.setJsUnitVersion("2.5");
        browserResult.setId("An ID");
        browserResult.setUserAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
        browserResult.setRemoteAddress("Dummy Remote Address");
        browserResult.setBaseURL("http://www.example.com/");
        browserResult.setTime(4.3);
        return browserResult;
    }

    private void assertFields(BrowserResult aResult) {
        assertEquals("2.5", aResult.getJsUnitVersion());
        assertEquals("An ID", aResult.getId());
        assertEquals("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)", aResult.getUserAgent());
        assertEquals("Dummy Remote Address", aResult.getRemoteAddress());
        assertEquals(4.3d, aResult.getTime(), 0.001d);
        assertEquals(3, aResult.getTestCaseResults().size());
        for (TestCaseResult testCaseResult : aResult.getTestCaseResults()) {
            assertNotNull(testCaseResult);
        }
    }

}