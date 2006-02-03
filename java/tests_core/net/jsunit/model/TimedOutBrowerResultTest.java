package net.jsunit.model;

import junit.framework.TestCase;

public class TimedOutBrowerResultTest extends TestCase {
	
	public void testSimple() {
		TimedOutBrowserResult result = new TimedOutBrowserResult("mybrowser.exe");
		assertEquals("mybrowser.exe", result.getUserAgent());
		assertEquals(ResultType.TIMED_OUT, result.getResultType());
	}

}
