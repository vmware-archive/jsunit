package net.jsunit;

import java.util.List;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.ResultType;

import org.jdom.Element;
import junit.framework.TestCase;

public class TimeoutCheckerTest extends TestCase {

	private MockBrowserTestRunner mockRunner;
	private TimeoutChecker checker;

	public void setUp() throws Exception {
		super.setUp();
		JsUnitServer.MAX_SECONDS_TO_WAIT = Integer.MAX_VALUE;
		mockRunner = new MockBrowserTestRunner();
		checker = new TimeoutChecker("mybrowser.exe", 1, mockRunner, 1);
		checker.start();
	}
	
	public void tearDown() throws Exception {
		if (checker != null && checker.isAlive()) {
			checker.die();
		}
		JsUnitServer.MAX_SECONDS_TO_WAIT=60;
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
		JsUnitServer.MAX_SECONDS_TO_WAIT =0;
		Thread.sleep(10);
		assertEquals(ResultType.TIMED_OUT, mockRunner.result.getResultType());
	}
	
	public void testNotTimeOut() throws InterruptedException {
		mockRunner.hasReceivedResult = false;
		Thread.sleep(10);
		assertTrue(checker.isAlive());
		mockRunner.hasReceivedResult = true;
		Thread.sleep(10);
		assertFalse(checker.isAlive());
	}
	
	static class MockBrowserTestRunner implements BrowserTestRunner {

		public BrowserResult result;
		private boolean hasReceivedResult;

		public void startTestRun() {		
		}

		public void finishTestRun() {		
		}

		public long launchTestRunForBrowserWithFileName(String browserFileName) {
			return 0;
		}

		public void accept(BrowserResult result) {
			this.result = result;
		}

		public boolean hasReceivedResultSince(long launchTime) {
			return hasReceivedResult;
		}

		public BrowserResult lastResult() {
			return null;
		}

		public void dispose() {
		}

		public BrowserResult findResultWithId(String id) {
			return null;
		}

		public void logStatus(String message) {
		}

		public List<String> getBrowserFileNames() {
			return null;
		}

		public Element asXml() {
			return null;
		}
		
	}
	
}
