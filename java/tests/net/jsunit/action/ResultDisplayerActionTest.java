package net.jsunit.action;

import java.util.Date;
import java.util.List;

import net.jsunit.BrowserTestRunner;
import net.jsunit.FailedToLaunchBrowserException;
import net.jsunit.model.BrowserResult;
import junit.framework.TestCase;

public class ResultDisplayerActionTest extends TestCase {

	private ResultDisplayerAction action;
	private MockBrowserTestRunner mockRunner;

	public void setUp() throws Exception {
		super.setUp();
		action = new ResultDisplayerAction();
		mockRunner = new MockBrowserTestRunner();
		action.setBrowserTestRunner(mockRunner);
		action.setId("12345");
	}
	
	public void testResultFound() throws Exception {
		mockRunner.result = new BrowserResult();
		assertEquals(ResultDisplayerAction.SUCCESS, action.execute());
		assertEquals("12345", mockRunner.idPassed);
		assertEquals(mockRunner.result, action.getXmlRenderable());
	}
	
	public void testResultNotFound() throws Exception {
		assertEquals(ResultDisplayerAction.SUCCESS, action.execute());
		assertEquals("12345", mockRunner.idPassed);
		assertEquals("<error>No Test Result has been submitted with ID 12345</error>", action.getXmlRenderable().asXml());		
	}

	public void testIdNotGiven() throws Exception {
		action.setId(null);
		assertEquals(ResultDisplayerAction.SUCCESS, action.execute());
		assertNull(mockRunner.idPassed);
		assertEquals("<error>No ID given</error>", action.getXmlRenderable().asXml());		
	}

	static class MockBrowserTestRunner implements BrowserTestRunner {

		private String idPassed;
		private BrowserResult result;

		public List<String> getBrowserFileNames() {
			return null;
		}

		public void launchTestRunForBrowserWithFileName(String browserFileName) throws FailedToLaunchBrowserException {		
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
			idPassed = id;
			return result;
		}
		
	}
}
