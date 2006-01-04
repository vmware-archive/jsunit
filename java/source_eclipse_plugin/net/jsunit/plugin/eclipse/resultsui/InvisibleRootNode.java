package net.jsunit.plugin.eclipse.resultsui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InvisibleRootNode extends ParentNode {

	public InvisibleRootNode() {
		super("");
	}

	protected String getStatus() {
		return null;
	}
	
	public String getImageName() {
		return null;
	}

	public List<BrowserResultNode> getBrowserResultChildrenNodes() {
		List<BrowserResultNode> result = new ArrayList<BrowserResultNode>();
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			result.add((BrowserResultNode) it.next());
		}
		return result;
	}

}
