package net.jsunit.model;

import junit.framework.TestCase;

public abstract class BrowserResultTestCase extends TestCase {

    protected BrowserResult result;

    protected String expectedXmlFragment =
            "<browserResult type=\"ERROR\" id=\"12345\" time=\"4.3\">" +
                    "<browser>" +
                    "<fullFileName>c:\\Program Files\\Internet Explorer\\iexplore.exe</fullFileName>" +
                    "<id>7</id>" +
                    "<displayName>Internet Explorer</displayName>" +
                    "<logoPath>/jsunit/images/logo_ie.gif</logoPath>" +
                    "</browser>" +
                    "<properties>" +
                    "<property name=\"jsUnitVersion\" value=\"2.5\" />" +
                    "<property name=\"userAgent\" value=\"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)\" />" +
                    "<property name=\"remoteAddress\" value=\"123.45.67.8\" />" +
                    "<property name=\"url\" value=\"http://www.example.com/\" />" +
                    "</properties>" +
                    "<testCaseResults>" +
                    "<testCaseResult type=\"SUCCESS\" name=\"page1.html:testFoo\" time=\"1.3\" />" +
                    "<testCaseResult type=\"ERROR\" name=\"page1.html:testFoo\" time=\"1.3\">" +
                    "<error>Test Error Message</error>" +
                    "</testCaseResult>" +
                    "<testCaseResult type=\"FAILURE\" name=\"page2.html:testFoo\" time=\"1.3\">" +
                    "<failure>Test Failure Message</failure>" +
                    "</testCaseResult>" +
                    "</testCaseResults>" +
                    "</browserResult>";
    private Browser browser;

    public void setUp() throws Exception {
        super.setUp();
        browser = new Browser("c:\\Program Files\\Internet Explorer\\iexplore.exe", 7);
        result = createBrowserResult();
        result._setTestCaseStrings(new String[]{
                "page1.html:testFoo|1.3|S||",
                "page1.html:testFoo|1.3|E|Test Error Message|",
                "page2.html:testFoo|1.3|F|Test Failure Message|"}
        );
    }

    protected BrowserResult createBrowserResult() {
        BrowserResult browserResult = new BrowserResult();
        browserResult.setBrowser(browser);
        browserResult.setJsUnitVersion("2.5");
        browserResult.setId("12345");
        browserResult.setUserAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
        browserResult.setRemoteAddress("123.45.67.8");
        browserResult.setBaseURL("http://www.example.com/");
        browserResult.setTime(4.3);
        return browserResult;
    }

    protected void assertFields(BrowserResult aResult) {
        assertEquals(browser, aResult.getBrowser());
        assertEquals("2.5", aResult.getJsUnitVersion());
        assertEquals("12345", aResult.getId());
        assertEquals("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)", aResult.getUserAgent());
        assertEquals("123.45.67.8", aResult.getRemoteAddress());
        assertEquals(4.3d, aResult.getTime(), 0.001d);
        assertEquals(3, aResult._getTestCaseResults().size());
        for (TestCaseResult testCaseResult : aResult._getTestCaseResults()) {
            assertNotNull(testCaseResult);
        }
    }
}
