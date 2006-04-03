package net.jsunit.model;

import java.util.ArrayList;
import java.util.List;

public class TestPageResult extends AbstractResult {

    private final String testPageName;
    private List<TestCaseResult> testCaseResults = new ArrayList<TestCaseResult>();

    public TestPageResult(String testPageName) {
        this.testPageName = testPageName;
    }

    public void addTestCaseResult(TestCaseResult testCaseResult) {
        testCaseResults.add(testCaseResult);
    }

    public String getTestPageName() {
        return testPageName;
    }

    public List<TestCaseResult> getTestCaseResults() {
        return testCaseResults;
    }

    protected List<? extends Result> getChildren() {
        return testCaseResults;
    }

    public void addErrorStringTo(StringBuffer buffer) {
        if (!wasSuccessful()) {
            buffer.append(getTestPageName());
            buffer.append("\n");
            for (TestCaseResult testCaseResult : testCaseResults)
                testCaseResult.addErrorStringTo(buffer);
            buffer.append("\n");
        }
    }

}
