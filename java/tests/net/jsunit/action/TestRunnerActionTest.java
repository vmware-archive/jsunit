package net.jsunit.action;

import net.jsunit.StandaloneTest;
import net.jsunit.Utility;
import junit.framework.TestCase;

public class TestRunnerActionTest extends TestCase {

	private TestRunnerAction action;

	public void setUp() throws Exception {
		super.setUp();
		action = new TestRunnerAction();
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
}
