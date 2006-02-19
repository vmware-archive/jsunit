package net.jsunit.plugin.eclipse.resultsui;

import java.util.List;

import net.jsunit.plugin.eclipse.resultsui.node.Node;
import net.jsunit.plugin.eclipse.resultsui.node.TestCaseResultNode;
import junit.framework.TestCase;

public class NodeLabelProviderTest extends TestCase {

	public void testSimple() {
		NodeLabelProvider provider = new NodeLabelProvider();
		assertTrue(provider.isFullyQualified());
		provider.getText(new MockNode());
	}
	
	static class MockNode extends Node {

		public MockNode() {
			super("Mock Node");
		}

		protected String getStatus() {
			return null;
		}

		public String getImageName() {
			return null;
		}

		public List<TestCaseResultNode> getProblemTestCaseResultNodes() {
			return null;
		}
		
	}
	
}
