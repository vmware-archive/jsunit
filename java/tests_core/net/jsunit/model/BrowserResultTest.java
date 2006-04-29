package net.jsunit.model;

import java.net.URL;
import java.util.List;

public class BrowserResultTest extends BrowserResultTestCase {

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
                "Internet Explorer",
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

    public void testLogUrl() throws Exception {
        BrowserResult result = createBrowserResult();
        URL logUrl = result.getLogUrl(new URL("http://mac.jsunit.net/jsunit"));
        assertEquals("http://mac.jsunit.net/jsunit/displayer?id=12345&browserId=7", logUrl.toString());
    }

}