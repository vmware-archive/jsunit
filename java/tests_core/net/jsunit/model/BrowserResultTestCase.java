package net.jsunit.model;

import junit.framework.TestCase;

public abstract class BrowserResultTestCase extends TestCase {

    protected BrowserResult result;

    protected String expectedXmlFragment =
            "<browserResult type=\"ERROR\" id=\"An ID\" time=\"4.3\">" +
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

    public void setUp() throws Exception {
        super.setUp();
        result = createBrowserResult();
        result.setTestCaseStrings(new String[]{
                "page1.html:testFoo|1.3|S||",
                "page1.html:testFoo|1.3|E|Test Error Message|",
                "page2.html:testFoo|1.3|F|Test Failure Message|"}
        );
    }

    protected BrowserResult createBrowserResult() {
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

    protected void assertFields(BrowserResult aResult) {
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
