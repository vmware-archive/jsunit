package net.jsunit;

import java.util.ArrayList;
import java.util.List;

import net.jsunit.model.BrowserResult;
import net.jsunit.model.ResultType;

import org.jdom.Element;

public class TestRunResult implements XmlRenderable {
    private List<BrowserResult> browserResults = new ArrayList<BrowserResult>();

    public int getFailureCount() {
        int count = 0;
        for (BrowserResult result : browserResults)
            count += result.failureCount();
        return count;
    }

    public int getErrorCount() {
        int count = 0;
        for (BrowserResult result : browserResults)
            count += result.errorCount();
        return count;
    }

    public boolean wasSuccessful() {
        for (BrowserResult result : browserResults)
            if (result.failureCount()>0 || result.errorCount() >0)
                return false;
        return true;
    }

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

	public ResultType getResultType() {
        if (getErrorCount() > 0)
            return ResultType.ERROR;
        if (getFailureCount() > 0)
            return ResultType.FAILURE;
        return ResultType.SUCCESS;
	}
}
