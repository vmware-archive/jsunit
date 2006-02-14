package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.MockImageSource;
import junit.framework.TestCase;

public class ExpandAllActionTest extends TestCase {

	public void testSimple() {
		MockActiveTabSource source = new MockActiveTabSource();
		ExpandAllAction action = new ExpandAllAction(source, new MockImageSource());
		assertFalse(source.tab.wasExpandAllCalled);
		action.run();
		assertTrue(source.tab.wasExpandAllCalled);
	}
	
}
