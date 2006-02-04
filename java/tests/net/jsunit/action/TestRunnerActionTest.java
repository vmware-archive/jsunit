package net.jsunit.action;

import java.util.List;

import junit.framework.TestCase;
import net.jsunit.BrowserTestRunner;
import net.jsunit.StandaloneTest;
import net.jsunit.Utility;
import net.jsunit.model.BrowserResult;

import org.jdom.Element;

public class TestRunnerActionTest extends TestCase {

	private TestRunnerAction action;

	public void setUp() throws Exception {
		super.setUp();
		action = new TestRunnerAction();
		action.setBrowserTestRunner(new MockBrowserTestRunner());
	}
	
	public void testSuccess() {
		DummySuccessfulStandaloneTest test = new DummySuccessfulStandaloneTest("testStandaloneRun");
		action.setStandaloneTest(test);
		assertEquals(TestRunnerAction.SUCCESS, action.execute());
		assertTrue(test.wasExecuted);
		assertEquals("<result>success</result>", Utility.asString(action.getXmlRenderable().asXml()));
	}
	
	public void testFailure() {

		DummyFailingStandaloneTest test = new DummyFailingStandaloneTest("testStandaloneRun");
		action.setStandaloneTest(test);
		assertEquals(TestRunnerAction.SUCCESS, action.execute());
		assertTrue(test.wasExecuted);
		assertEquals("<result>failure</result>", Utility.asString(action.getXmlRenderable().asXml()));
	}
	
	public static class DummySuccessfulStandaloneTest extends StandaloneTest {
		
		private boolean wasExecuted;

		public DummySuccessfulStandaloneTest(String name) {
			super(name);
		}
		
		public void setUp() {
		}
		
		public void tearDown() {
		}

		public void testStandaloneRun() {
			wasExecuted = true;
		}
	}

	public static class DummyFailingStandaloneTest extends StandaloneTest {
		
		private boolean wasExecuted;

		public DummyFailingStandaloneTest(String name) {
			super(name);
		}
		
		public void setUp() {
		}
		
		public void tearDown() {
		}

		public void testStandaloneRun() {
			wasExecuted = true;
			fail();
		}
	}
	
	static class MockBrowserTestRunner implements BrowserTestRunner {

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
