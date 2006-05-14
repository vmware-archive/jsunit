package net.jsunit.model;

import net.jsunit.XmlRenderable;
import org.jdom.Document;
import org.jdom.Element;

import java.util.*;

public class DistributedTestRunResult extends AbstractResult implements XmlRenderable, Iterable<TestRunResult> {

    public static final String NAME = "distributedTestRunResult";

    private List<TestRunResult> testRunResults = new ArrayList<TestRunResult>();

    protected List<? extends Result> getChildren() {
        return testRunResults;
    }

    public void addTestRunResult(TestRunResult result) {
        testRunResults.add(result);
        Collections.sort(testRunResults);
    }

    public Element asXml() {
        Element root = new Element(NAME);
        root.setAttribute("type", _getResultType().name());
        for (TestRunResult testRunResult : testRunResults)
            root.addContent(testRunResult.asXml());
        return root;
    }

    public List<TestRunResult> _getTestRunResults() {
        return testRunResults;
    }

    public TestRunResult[] getTestRunResults() {
        return testRunResults.toArray(new TestRunResult[testRunResults.size()]);
    }

    public void setTestRunResults(TestRunResult[] results) {
        testRunResults = Arrays.asList(results);
    }

    public Document asXmlDocument() {
        return new Document(asXml());
    }

    public Iterator<TestRunResult> iterator() {
        return _getTestRunResults().iterator();
    }

    public BrowserResult findBrowserResultMatching(BrowserSpecification spec) {
        for (TestRunResult testRunResult : this) {
            if (spec.matches(testRunResult))
                return testRunResult.findBrowserResultMatching(spec);
        }
        return null;
    }

}
