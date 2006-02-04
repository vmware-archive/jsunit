package net.jsunit.model;

import junit.framework.TestCase;

public class TimedOutBrowerResultTest extends TestCase {
	
    private static String xml = 
    	"<browserResult timedOut=\"true\">" +
    		"<properties>" +
        		"<property name=\"browserFileName\" value=\"c:\\Program Files\\Internet Explorer\\iexplore.exe\" />" +
            "</properties>" +
        "</browserResult>";

    private TimedOutBrowserResult browserResult;
	
	public void setUp() throws Exception {
		super.setUp();
		browserResult = new TimedOutBrowserResult();
		browserResult.setBrowserFileName("c:\\Program Files\\Internet Explorer\\iexplore.exe");
	}
	
	public void testSimple() {
		assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", browserResult.getBrowserFileName());
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
		BrowserResult reconstitutedResult = new BrowserResultBuilder().build(xml);
		assertEquals(TimedOutBrowserResult.class, reconstitutedResult.getClass());
		assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", reconstitutedResult.getBrowserFileName());
	}

}
