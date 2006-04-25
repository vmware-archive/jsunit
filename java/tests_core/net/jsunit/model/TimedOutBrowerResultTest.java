package net.jsunit.model;

import junit.framework.TestCase;

public class TimedOutBrowerResultTest extends TestCase {

    private String xml =
            "<browserResult type=\"TIMED_OUT\">" +
                    "<properties>" +
                    "<property name=\"browserFileName\" value=\"c:\\Program Files\\Internet Explorer\\iexplore.exe\" />" +
                    "<property name=\"browserId\" value=\"3\" />" +
                    "</properties>" +
                    "</browserResult>";

    private BrowserResult browserResult;

    public void setUp() throws Exception {
        super.setUp();
        browserResult = new BrowserResult();
        browserResult.setResultType(ResultType.TIMED_OUT);
        browserResult.setBrowser(new Browser("c:\\Program Files\\Internet Explorer\\iexplore.exe", 3));
    }

    public void testSimple() {
        assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", browserResult.getBrowser().getStartCommand());
        assertEquals(ResultType.TIMED_OUT, browserResult.getResultType());
    }

    public void testCompleted() {
        assertFalse(browserResult.completedTestRun());
        assertTrue(browserResult.timedOut());
        assertFalse(browserResult.failedToLaunch());
    }

    public void testAsXml() {
        assertEquals(xml, browserResult.asXmlFragment());
    }

    public void testReconstituteFromXml() {
        BrowserResultBuilder builder = new BrowserResultBuilder();
        BrowserResult reconstitutedResult = builder.build(xml);
        assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", reconstitutedResult.getBrowser().getStartCommand());
        assertTrue(reconstitutedResult.timedOut());
        assertEquals(ResultType.TIMED_OUT, reconstitutedResult.getResultType());

    }

}
