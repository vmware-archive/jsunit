package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.model.ResultType;

public class TimeoutCheckerTest extends TestCase {

	private MockBrowserTestRunner mockRunner;
	private TimeoutChecker checker;

	public void setUp() throws Exception {
		super.setUp();
		mockRunner = new MockBrowserTestRunner();
		mockRunner.timeoutSeconds = Integer.MAX_VALUE;
		checker = new TimeoutChecker("mybrowser.exe", 1, mockRunner, 1);
		checker.start();
	}
	
	public void tearDown() throws Exception {
		if (checker != null && checker.isAlive()) {
			checker.die();
		}
		super.tearDown();
	}
	
	public void testInitialConditions() {
		assertTrue(checker.isAlive());
	}
	
	public void testDie() throws InterruptedException {
		checker.die();
		Thread.sleep(10);
		assertFalse(checker.isAlive());		
	}
	
	public void testTimeOut() throws InterruptedException {
		mockRunner.timeoutSeconds = 0;
		Thread.sleep(10);
		assertEquals(ResultType.TIMED_OUT, mockRunner.acceptedResult.getResultType());
	}
	
	public void testNotTimeOut() throws InterruptedException {
		mockRunner.hasReceivedResult = false;
		Thread.sleep(10);
		assertTrue(checker.isAlive());
		mockRunner.hasReceivedResult = true;
		Thread.sleep(10);
		assertFalse(checker.isAlive());
	}
	
}
