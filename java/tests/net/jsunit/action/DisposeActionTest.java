package net.jsunit.action;

import java.util.Date;
import java.util.List;

import org.jdom.Element;

import net.jsunit.BrowserTestRunner;
import net.jsunit.model.BrowserResult;
import junit.framework.TestCase;

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

		public void launchTestRunForBrowserWithFileName(String browserFileName) {
		}

		public void accept(BrowserResult result) {
		}

		public boolean hasReceivedResultSince(Date dateBrowserLaunched) {
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
