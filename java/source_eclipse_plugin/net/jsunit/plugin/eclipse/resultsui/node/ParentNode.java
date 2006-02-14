package net.jsunit.plugin.eclipse.resultsui.node;

import java.util.ArrayList;
import java.util.List;

public abstract class ParentNode extends Node {
	
	private List<Node> children;
	
	public ParentNode(String name) {
		super(name);
		children = new ArrayList<Node>();
	}
	
	public void addChild(Node child) {
		children.add(child);
		child.setParent(this);
	}
	
	public void removeChild(Node child) {
		children.remove(child);
		child.setParent(null);
	}
	
	public List<Node> getChildren() {
		return new ArrayList<Node>(children);
	}
	
	public boolean hasChildren() {
		return children.size()>0;
	}
	
	public List<TestCaseResultNode> getProblemTestCaseResultNodes() {
		List<TestCaseResultNode> result = new ArrayList<TestCaseResultNode>();
		for (Node node : getChildren())
			result.addAll(node.getProblemTestCaseResultNodes());
		return result;
	}
	
}