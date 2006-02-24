package net.jsunit.model;

import net.jsunit.XmlRenderable;
import org.jdom.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TestRunResult extends AbstractResult implements XmlRenderable {

    private List<BrowserResult> browserResults = new ArrayList<BrowserResult>();
    private URL url;
    private boolean unresponsive = false;

    public TestRunResult() {
        this(null);
    }

    public TestRunResult(URL url) {
        this.url = url;
    }

    public void addBrowserResult(BrowserResult browserResult) {
        browserResults.add(browserResult);
    }

    public Element asXml() {
        Element root = new Element("testRunResult");
        root.setAttribute("type", getResultType().name());
        if (url != null)
            root.setAttribute("url", url.toString());
        for (BrowserResult browserResult : browserResults)
            root.addContent(browserResult.asXml());
        return root;
    }

    protected List<? extends Result> getChildren() {
        return browserResults;
    }

    public void setUnresponsive() {
        unresponsive = true;
    }

    public boolean wasUnresponsive() {
        return unresponsive;
    }

    public URL getUrl() {
        return url;
    }

    public ResultType getResultType() {
        if (unresponsive)
            return ResultType.UNRESPONSIVE;
        else
            return super.getResultType();
    }
}
