package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.MockImageSource;
import net.jsunit.plugin.eclipse.resultsui.NodeLabelProvider;
import junit.framework.TestCase;

public class ToggleFullyQualifiedNodeNamesActionTest extends TestCase {

	public void testSimple() {
		NodeLabelProvider labelProvider = new NodeLabelProvider();
		MockActiveTabSource source = new MockActiveTabSource();
		ToggleFullyQualifiedNodeNamesAction action = new ToggleFullyQualifiedNodeNamesAction(source, new MockImageSource(), labelProvider);
		assertFalse(labelProvider.isFullyQualified());
		assertFalse(action.isChecked());
		
		action.run();
		assertTrue(labelProvider.isFullyQualified());
		assertTrue(action.isChecked());
		assertTrue(source.tab.wasRefreshCalled);

		action.run();
		assertFalse(labelProvider.isFullyQualified());
		assertFalse(action.isChecked());
	}
	
}
