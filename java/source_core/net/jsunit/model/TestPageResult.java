package net.jsunit.model;

import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

public class TestPageResult extends AbstractResult {

    private String testPageName;
    private List<TestCaseResult> testCaseResults = new ArrayList<TestCaseResult>();

    public TestPageResult() {
    }

    public TestPageResult(String testPageName) {
        this.testPageName = testPageName;
    }

    public void addTestCaseResult(TestCaseResult testCaseResult) {
        testCaseResults.add(testCaseResult);
    }

    public String getTestPageName() {
        return testPageName;
    }

    public void setTestPageName(String name) {
        this.testPageName = name;
    }

    public List<TestCaseResult> _getTestCaseResults() {
        return testCaseResults;
    }

    public TestCaseResult[] getTestCaseResults() {
        return testCaseResults.toArray(new TestCaseResult[testCaseResults.size()]);
    }

    protected List<? extends Result> getChildren() {
        return testCaseResults;
    }

    protected void addMyErrorStringTo(StringBuffer buffer) {
        if (!wasSuccessful()) {
            buffer.append(getTestPageName());
            buffer.append("\n");
        }

    }

    public Element asXml() {
        return null;
    }
}
