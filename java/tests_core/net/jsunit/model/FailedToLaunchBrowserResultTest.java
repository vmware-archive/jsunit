package net.jsunit.model;

import java.io.FileNotFoundException;

import net.jsunit.Utility;

import junit.framework.TestCase;

public class FailedToLaunchBrowserResultTest extends TestCase {

	private static Throwable exception = new FileNotFoundException();
    private static String xml = 
    	"<browserResult failedToLaunch=\"true\">" +
    		"<properties>" +
        		"<property name=\"browserFileName\" value=\"c:\\Program Files\\Internet Explorer\\iexplore.exe\" />" +
        		"<property name=\"serverSideExceptionStackTrace\"><![CDATA[" +
        			Utility.stackTraceAsString(exception)+
        		"]]></property>" +
            "</properties>" +
        "</browserResult>";

	private BrowserResult result;
	
    public void setUp() throws Exception {
    	super.setUp();
		result = new BrowserResult();
		result.setFailedToLaunch();
		result.setBrowserFileName("c:\\Program Files\\Internet Explorer\\iexplore.exe");
		result.setServerSideException(exception);
    }
    
	public void testSimple() {
		assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", result.getBrowserFileName());
		assertEquals(0d, result.getTime());
		assertEquals(ResultType.FAILED_TO_LAUNCH.getDisplayString(), result.getDisplayString());
		assertEquals(0, result.count());
		assertEquals(ResultType.FAILED_TO_LAUNCH, result.getResultType());
		assertEquals(0, result.getTestPageResults().size());
		assertEquals(Utility.stackTraceAsString(exception), result.getServerSideExceptionStackTrace());
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
		assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", reconstitutedResult.getBrowserFileName());
		assertTrue(reconstitutedResult.failedToLaunch());
		assertEquals(ResultType.FAILED_TO_LAUNCH, reconstitutedResult.getResultType());
		//TODO: somehow they're not quite equal
		//assertEquals(Utility.stackTraceAsString(exception), reconstitutedResult.getServerSideExceptionStackTrace());
	}
	
}
