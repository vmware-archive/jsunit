package net.jsunit;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import net.jsunit.model.BrowserResult;

public class TestRunManagerTest extends TestCase {
	
	public void testSuccess() throws Exception {
		TestRunManager manager = new TestRunManager(new SuccessfulBrowserTestRunner());
		manager.runTests();
		assertFalse(manager.hadProblems());
		assertEquals(0, manager.getErrorCount());
		assertEquals(0, manager.getFailureCount());
	}
	
	public void testFailure() throws Exception {
		TestRunManager manager = new TestRunManager(new FailingBrowserTestRunner());
		manager.runTests();
		assertTrue(manager.hadProblems());
		assertEquals(4, manager.getErrorCount());
		assertEquals(3, manager.getFailureCount());		
	}
	
	public void testCrash() throws Exception {
		TestRunManager manager = new TestRunManager(new CrashingBrowserTestRunner());
		try {
			manager.runTests();
			fail("Should have crashed");
		} catch (FailedToLaunchBrowserException e) {
		}
	}
	
	static class SuccessfulBrowserTestRunner implements BrowserTestRunner {

		public List<String> getBrowserFileNames() {
			return Arrays.asList(new String[] {"browser1.exe", "browser2.exe"});
		}

		public void launchTestRunForBrowserWithFileName(String browserFileName) throws FailedToLaunchBrowserException {
		}

		public boolean hasReceivedResultSince(Date dateBrowserLaunched) {
			return true;
		}

		public BrowserResult lastResult() {
			return new DummyBrowserResult(true, 0, 0);
		}

		public void accept(BrowserResult result) {
		}

		public void dispose() {
		}

		public BrowserResult findResultWithId(String id) {
			return null;
		}

	}
	
	static class FailingBrowserTestRunner implements BrowserTestRunner {

		private String currentBrowser;

		public List<String> getBrowserFileNames() {
			return Arrays.asList(new String[] {"browser1.exe", "browser2.exe", "browser3.exe"});
		}

		public void launchTestRunForBrowserWithFileName(String browserFileName) throws FailedToLaunchBrowserException {
			currentBrowser = browserFileName;
		}

		public boolean hasReceivedResultSince(Date dateBrowserLaunched) {
			return true;
		}

		public BrowserResult lastResult() {
			if (currentBrowser.indexOf("1") !=-1)
				return new DummyBrowserResult(false, 0, 1); 
			else if (currentBrowser.indexOf("2") !=-1)
				return new DummyBrowserResult(false, 1, 0);
			else
				return new DummyBrowserResult(false, 2, 3);
		}

		public void accept(BrowserResult result) {
		}

		public void dispose() {
		}

		public BrowserResult findResultWithId(String id) {
			return null;
		}
	}
	
	static class CrashingBrowserTestRunner implements BrowserTestRunner {

		public List<String> getBrowserFileNames() {
			return Arrays.asList(new String[] {"browser.exe"});
		}

		public void launchTestRunForBrowserWithFileName(String browserFileName) throws FailedToLaunchBrowserException {
			throw new FailedToLaunchBrowserException("dummy");
		}

		public boolean hasReceivedResultSince(Date dateBrowserLaunched) {
			return false;
		}

		public BrowserResult lastResult() {
			return null;
		}

		public void accept(BrowserResult result) {
		}

		public void dispose() {
		}

		public BrowserResult findResultWithId(String id) {
			return null;
		}
		
	}
	
}