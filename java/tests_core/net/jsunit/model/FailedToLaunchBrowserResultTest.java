package net.jsunit.model;

import java.io.FileNotFoundException;

import junit.framework.TestCase;

public class FailedToLaunchBrowserResultTest extends TestCase {

    private static String xml = 
    	"<browserResult failedToLaunch=\"true\">" +
    		"<properties>" +
        		"<property name=\"browserFileName\" value=\"c:\\Program Files\\Internet Explorer\\iexplore.exe\" />" +
            "</properties>" +
        "</browserResult>";

    private Throwable throwable;
	private FailedToLaunchBrowserResult result;
	
    public void setUp() throws Exception {
    	super.setUp();
		throwable = new FileNotFoundException();
		result = new FailedToLaunchBrowserResult();
		result.setBrowserFileName("c:\\Program Files\\Internet Explorer\\iexplore.exe");
		result.setServerSideJavaException(throwable);
    }
    
	public void testSimple() {
		assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", result.getBrowserFileName());
		assertEquals(0d, result.getTime());
		assertEquals(ResultType.FAILED_TO_LAUNCH.getDisplayString() + " (" + throwable.getClass().getName()+")", result.getDisplayString());
		assertEquals(0, result.count());
		assertEquals(ResultType.FAILED_TO_LAUNCH, result.getResultType());
		assertEquals(0, result.getTestPageResults().size());
	}
	
	public void testCompleted() {
		assertFalse(result.completedTestRun());
		assertFalse(result.timedOut());
		assertTrue(result.failedToLaunch());
	}
	
	public void testXml() {
		assertEquals(xml, result.asXmlFragment());
	}
	
	public void testReconstituteFromXml() {
		BrowserResult reconstitutedResult = new BrowserResultBuilder().build(xml);
		assertEquals(FailedToLaunchBrowserResult.class, reconstitutedResult.getClass());
		assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", reconstitutedResult.getBrowserFileName());
	}
	
}
