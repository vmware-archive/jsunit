package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.MockImageSource;
import junit.framework.TestCase;

public class CollapseAllActionTest extends TestCase {

	public void testSimple() {
		MockActiveTabSource source = new MockActiveTabSource();
		CollapseAllAction action = new CollapseAllAction(source, new MockImageSource());
		assertFalse(source.tab.wasCollapseAllCalled);
		action.run();
		assertTrue(source.tab.wasCollapseAllCalled);
	}
	
}
