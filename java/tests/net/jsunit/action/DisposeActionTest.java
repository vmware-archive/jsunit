package net.jsunit.action;

import java.util.List;

import junit.framework.TestCase;
import net.jsunit.BrowserTestRunner;
import net.jsunit.model.BrowserResult;

import org.jdom.Element;

public class DisposeActionTest extends TestCase {
	
	public void testSimple() throws Exception {
		DisposeAction action = new DisposeAction();
		MockBrowserTestRunner runner = new MockBrowserTestRunner();
		action.setBrowserTestRunner(runner);
		assertEquals(DisposeAction.SUCCESS, action.execute());
		assertTrue(runner.wasDisposedCalled);
	}
	
	static class MockBrowserTestRunner implements BrowserTestRunner {

		public boolean wasDisposedCalled;

		public void startTestRun() {		
		}

		public void finishTestRun() {
		}

		public long launchTestRunForBrowserWithFileName(String browserFileName) {
			return 0;
		}

		public void accept(BrowserResult result) {
		}

		public boolean hasReceivedResultSince(long launchTime) {
			return false;
		}

		public BrowserResult lastResult() {
			return null;
		}

		public void dispose() {
			wasDisposedCalled = true;
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
