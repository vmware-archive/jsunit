package net.jsunit.plugin.eclipse.resultsui;

abstract class Node {
	
	private String name;
	private ParentNode parent;
	
	public Node(String name) {
		this.name = name;
	}
	
	protected abstract String getStatus();
	
	public String getName() {
		return name;
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
	
	public String getDisplayString() {
		String result = getName();
		if (getStatus() != null) {
			result += " (" + getStatus()+ ")";
		}
		return result;
	}

	public abstract String getImageName();

}