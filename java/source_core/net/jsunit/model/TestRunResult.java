package net.jsunit.model;

import net.jsunit.XmlRenderable;
import org.jdom.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TestRunResult extends AbstractResult implements XmlRenderable {
	
    private List<BrowserResult> browserResults = new ArrayList<BrowserResult>();
	private List<URL> crashedRemoteURLs = new ArrayList<URL>();

    public void addBrowserResult(BrowserResult browserResult) {
        browserResults.add(browserResult);
    }
    
    public ResultType getResultType() {
    	if (!crashedRemoteURLs.isEmpty())
    		return ResultType.TIMED_OUT;
    	return super.getResultType();
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

	public void addCrashedRemoteURL(URL url) {
		crashedRemoteURLs.add(url);
	}

    public List<URL> getCrashedRemoteURLs() {
        return crashedRemoteURLs;
    }
}
