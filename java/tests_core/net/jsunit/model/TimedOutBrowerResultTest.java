package net.jsunit.model;

import junit.framework.TestCase;

public class TimedOutBrowerResultTest extends TestCase {
	
    private static String xml = 
    	"<browserResult timedOut=\"true\">" +
    		"<properties>" +
        		"<property name=\"browserFileName\" value=\"c:\\Program Files\\Internet Explorer\\iexplore.exe\" />" +
            "</properties>" +
        "</browserResult>";

    private BrowserResult browserResult;
	
	public void setUp() throws Exception {
		super.setUp();
		browserResult = new BrowserResult();
		browserResult.setTimedOut();
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
    	assertFalse(browserResult.externallyShutDown());
    }
	
	public void testAsXml() {
		assertEquals(xml, browserResult.asXmlFragment());
	}
	
	public void testReconstituteFromXml() {
		BrowserResult reconstitutedResult = new BrowserResultBuilder().build(xml);
		assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", reconstitutedResult.getBrowserFileName());
		assertTrue(reconstitutedResult.timedOut());
		assertEquals(ResultType.TIMED_OUT, reconstitutedResult.getResultType());

	}

}
