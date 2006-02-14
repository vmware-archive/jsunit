package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.MockImageSource;
import net.jsunit.plugin.eclipse.resultsui.TestRunStoppable;
import junit.framework.TestCase;

public class StopActionTest extends TestCase {

	private MockTestRunStoppable stoppable;
	private StopAction action;

	public void setUp() throws Exception {
		super.setUp();
		stoppable = new MockTestRunStoppable();
		action = new StopAction(stoppable, new MockImageSource());
		action.setEnabled(true);
	}
	
	public void testInitialConditions() {
		assertFalse(stoppable.stopped);
		assertTrue(action.isEnabled());
	}
	
	public void testRun() {
		action.run();
		assertTrue(stoppable.stopped);
		assertFalse(action.isEnabled());		
	}
	
	static class MockTestRunStoppable implements TestRunStoppable {

		private boolean stopped;

		public void stopTestRun() {
			stopped = true;
		}
		
	}
	
}
