package net.jsunit.plugin.eclipse.resultsui;

import java.util.List;

import net.jsunit.plugin.eclipse.resultsui.node.Node;
import net.jsunit.plugin.eclipse.resultsui.node.TestCaseResultNode;
import junit.framework.TestCase;

public class NodeLabelProviderTest extends TestCase {

	public void testSimple() {
		NodeLabelProvider provider = new NodeLabelProvider();
		assertFalse(provider.isFullyQualified());
		MockNode mockNode = new MockNode();
		provider.getText(mockNode);
		assertFalse(mockNode.fullyQualified);
		
		provider.setFullyQualified(true);
		provider.getText(mockNode);
		assertTrue(mockNode.fullyQualified);
	}
	
	static class MockNode extends Node {

		private boolean fullyQualified;

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
		
		public String getDisplayString(boolean fullyQualified) {
			this.fullyQualified = fullyQualified;
			return null;
		}
		
	}
	
}
