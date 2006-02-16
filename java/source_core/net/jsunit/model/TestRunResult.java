package net.jsunit.model;

import java.util.ArrayList;
import java.util.List;

import net.jsunit.XmlRenderable;

import org.jdom.Element;

public class TestRunResult extends AbstractResult implements XmlRenderable {
	
    private List<BrowserResult> browserResults = new ArrayList<BrowserResult>();

    public void addBrowserResult(BrowserResult browserResult) {
        browserResults.add(browserResult);
    }

	public Element asXml() {
		Element root = new Element("testRunResult");
		root.setAttribute("type", getResultType().name());
		for (BrowserResult browserResult : browserResults)
			root.addContent(browserResult.asXml());
		return root;
	}

	protected List<? extends Result> getChildren() {
		return browserResults;
	}

	public void mergeWith(TestRunResult result) {
		browserResults.addAll(result.browserResults);		
	}
}
