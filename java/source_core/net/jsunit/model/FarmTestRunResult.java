package net.jsunit.model;

import net.jsunit.XmlRenderable;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

public class FarmTestRunResult extends AbstractResult implements XmlRenderable {

    private List<TestRunResult> testRunResults = new ArrayList<TestRunResult>();

    protected List<? extends Result> getChildren() {
        return testRunResults;
    }

    public void addTestRunResult(TestRunResult result) {
        testRunResults.add(result);
    }

    public Element asXml() {
        Element root = new Element("farmTestRunResult");
        root.setAttribute("type", getResultType().name());
        for (TestRunResult testRunResult : testRunResults)
            root.addContent(testRunResult.asXml());
        return root;
    }

    public List<TestRunResult> getTestRunResults() {
        return testRunResults;
    }

}
