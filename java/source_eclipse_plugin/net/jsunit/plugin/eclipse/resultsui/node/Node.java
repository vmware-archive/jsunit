package net.jsunit.plugin.eclipse.resultsui.node;

import java.util.List;

import net.jsunit.utility.StringUtility;

public abstract class Node {
	
	private String name;
	private ParentNode parent;
	
	public Node(String name) {
		this.name = name;
	}
	
	protected abstract String getStatus();
	
	public String getName() {
		return getName(true);
	}
	
	public String getName(boolean fullyQualified) {
		return fullyQualified ? name : StringUtility.unqualify(name);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setParent(ParentNode parent) {
		this.parent = parent;
	}
	
	public ParentNode getParent() {
		return parent;
	}
	
	public String getDisplayString(boolean fullyQualified) {
		String result = getName(fullyQualified);
		if (getStatus() != null) {
			result += " (" + getStatus()+ ")";
		}
		return result;
	}

	public abstract String getImageName();
	
	public abstract List<TestCaseResultNode> getProblemTestCaseResultNodes();

}