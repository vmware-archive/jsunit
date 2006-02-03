package net.jsunit.model;

import java.io.FileNotFoundException;

import junit.framework.TestCase;

public class FailedToLaunchBrowserResultTest extends TestCase {

	public void testSimple() {
		Throwable throwable = new FileNotFoundException();
		BrowserResult result = new FailedToLaunchBrowserResult("mybrowser.exe", throwable);
		assertEquals(0d, result.getTime());
		assertEquals("mybrowser.exe", result.getUserAgent());
		assertEquals(ResultType.FAILED_TO_LAUNCH.getDisplayString() + " (" + throwable.getClass().getName()+")", result.getDisplayString());
		assertEquals(0, result.count());
		assertEquals(ResultType.FAILED_TO_LAUNCH, result.getResultType());
		assertEquals(0, result.getTestPageResults().size());
	}
	
}
