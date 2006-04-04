package net.jsunit.model;

import junit.framework.TestCase;
import net.jsunit.utility.FileUtility;

import java.io.File;
import java.util.List;

public class BrowserResultTest extends TestCase {
    private BrowserResult result;

    private String expectedXmlFragment =
            "<browserResult id=\"An ID\" time=\"4.3\">" +
                    "<properties>" +
                    "<property name=\"browserFileName\" value=\"c:\\Program Files\\Internet Explorer\\iexplore.exe\" />" +
                    "<property name=\"browserId\" value=\"7\" />" +
                    "<property name=\"jsUnitVersion\" value=\"2.5\" />" +
                    "<property name=\"userAgent\" value=\"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)\" />" +
                    "<property name=\"remoteAddress\" value=\"123.45.67.8\" />" +
                    "<property name=\"url\" value=\"http://www.example.com/\" />" +
                    "</properties>" +
                    "<testCases>" +
                    "<testCase name=\"page1.html:testFoo\" time=\"1.3\" />" +
                    "<testCase name=\"page1.html:testFoo\" time=\"1.3\">" +
                    "<error>Test Error Message</error>" +
                    "</testCase>" +
                    "<testCase name=\"page2.html:testFoo\" time=\"1.3\">" +
                    "<failure>Test Failure Message</failure>" +
                    "</testCase>" +
                    "</testCases>" +
                    "</browserResult>";

    private BrowserSource browserSource = new DummyBrowserSource("c:\\Program Files\\Internet Explorer\\iexplore.exe", 7);

    public void setUp() throws Exception {
        super.setUp();
        result = createBrowserResult();
        result.setTestCaseStrings(new String[]{
                "page1.html:testFoo|1.3|S||",
                "page1.html:testFoo|1.3|E|Test Error Message|",
                "page2.html:testFoo|1.3|F|Test Failure Message|"}
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

    public void testDisplayString() {
        assertEquals(ResultType.ERROR.getDisplayString(), result.getDisplayString());
    }

    public void testBuildFromXmlFile() {
        File file = null;
        try {
            FileUtility.write(new File("resultXml.xml"), expectedXmlFragment);
            file = new File("resultXml.xml");
            BrowserResult reconstitutedResult = new BrowserResultBuilder(browserSource).build(file);
            assertEquals(BrowserResult.class, reconstitutedResult.getClass());
            assertFields(reconstitutedResult);
        } finally {
            if (file != null)
                file.delete();
        }
    }

    public void testBuildFromXmlDocument() {
        BrowserResult reconstitutedResult = new BrowserResultBuilder(browserSource).build(result.asXmlDocument());
        assertFields(reconstitutedResult);
    }

    public void testFailure() {
        BrowserResult result = createBrowserResult();
        result.setTestCaseStrings(new String[]{
                "page.html:testFoo|1.3|S||",
                "page.html:testBar|1.3|F|Test Failure Message|"
        });
        assertFalse(result.wasSuccessful());
        assertEquals(ResultType.FAILURE, result.getResultType());
        assertEquals("The test run had 0 error(s) and 1 failure(s).", result.displayString());
    }

    public void testSuccess() {
        BrowserResult result = createBrowserResult();
        result.setTestCaseStrings(new String[]{
                "page.html:testFoo|1.3|S||",
                "page.html:testBar|1.3|S||"
        });
        assertTrue(result.wasSuccessful());
        assertEquals(ResultType.SUCCESS, result.getResultType());
    }

    public void testGetTestPageResults() {
        List<TestPageResult> testPageResults = result.getTestPageResults();
        assertEquals(2, testPageResults.size());
        TestPageResult result1 = testPageResults.get(0);
        assertEquals("page1.html", result1.getTestPageName());
        assertEquals(2, result1.getTestCaseResults().size());
        TestPageResult result2 = testPageResults.get(1);
        assertEquals("page2.html", result2.getTestPageName());
        assertEquals(1, result2.getTestCaseResults().size());
    }

    public void testCompleted() {
        assertTrue(result.completedTestRun());
        assertFalse(result.timedOut());
        assertFalse(result.failedToLaunch());
        assertFalse(result.externallyShutDown());
    }

    public void testIsForBrowser() throws Exception {
        assertFalse(result.isForBrowser(new Browser("mybrowser.exe", 9)));
        assertFalse(result.isForBrowser(new Browser("c:\\Program Files\\Internet Explorer\\iexplore.exe", 9)));
        assertFalse(result.isForBrowser(new Browser("mybrowser.exe", 7)));
        assertTrue(result.isForBrowser(new Browser("c:\\Program Files\\Internet Explorer\\iexplore.exe", 7)));
    }

    public void testBrowserDisplayString() throws Exception {
        BrowserResult browserResult = createBrowserResult();
        assertEquals(
                "c:\\Program Files\\Internet Explorer\\iexplore.exe (ID 7) at 123.45.67.8",
                browserResult.getBrowserDisplayString()
        );
    }

    public void testAsErrorString() throws Exception {
        BrowserResult result = createBrowserResult();
        TestCaseResult error1 = TestCaseResult.fromString("file:///dummy/path/dummyPage1.html:testFoo|1.3|E|Test Error Message|");
        TestCaseResult success1 = TestCaseResult.fromString("file://dummy/path/dummyPage1.html:testFoo|1.3|S||");
        TestCaseResult failure1 = TestCaseResult.fromString("file:///dummy/path/dummyPage1.html:testFoo|1.3|F|Test Failure Message|");
        result.addTestCaseResult(error1);
        result.addTestCaseResult(failure1);
        result.addTestCaseResult(success1);

        TestCaseResult error2 = TestCaseResult.fromString("file:///dummy/path/dummyPage2.html:testFoo|1.3|E|Test Error Message|");
        TestCaseResult success2 = TestCaseResult.fromString("file://dummy/path/dummyPage2.html:testFoo|1.3|S||");
        TestCaseResult failure2 = TestCaseResult.fromString("file:///dummy/path/dummyPage2.html:testFoo|1.3|F|Test Failure Message|");
        result.addTestCaseResult(error2);
        result.addTestCaseResult(failure2);
        result.addTestCaseResult(success2);

        StringBuffer buffer = new StringBuffer();
        result.addErrorStringTo(buffer);

        StringBuffer expected = new StringBuffer();
        expected.append(result.getBrowserDisplayString());
        expected.append("\n");
        TestPageResult page1 = new TestPageResult("file:///dummy/path/dummyPage1.html");
        page1.addTestCaseResult(error1);
        page1.addTestCaseResult(failure1);
        page1.addTestCaseResult(success1);
        page1.addErrorStringTo(expected);

        expected.append("\n");

        TestPageResult page2 = new TestPageResult("file:///dummy/path/dummyPage2.html");
        page2.addTestCaseResult(error2);
        page2.addTestCaseResult(failure2);
        page2.addTestCaseResult(success2);
        page2.addErrorStringTo(expected);

        assertEquals(expected.toString(), buffer.toString());
    }

    private BrowserResult createBrowserResult() {
        BrowserResult browserResult = new BrowserResult();
        browserResult.setBrowser(new Browser("c:\\Program Files\\Internet Explorer\\iexplore.exe", 7));
        browserResult.setJsUnitVersion("2.5");
        browserResult.setId("An ID");
        browserResult.setUserAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
        browserResult.setRemoteAddress("123.45.67.8");
        browserResult.setBaseURL("http://www.example.com/");
        browserResult.setTime(4.3);
        return browserResult;
    }

    private void assertFields(BrowserResult aResult) {
        assertEquals("2.5", aResult.getJsUnitVersion());
        assertEquals("An ID", aResult.getId());
        assertEquals("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)", aResult.getUserAgent());
        assertEquals("123.45.67.8", aResult.getRemoteAddress());
        assertEquals(4.3d, aResult.getTime(), 0.001d);
        assertEquals(3, aResult.getTestCaseResults().size());
        for (TestCaseResult testCaseResult : aResult.getTestCaseResults()) {
            assertNotNull(testCaseResult);
        }
    }

}