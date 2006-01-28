package net.jsunit.model;

import java.util.ArrayList;
import java.util.List;

public class TestPageResult {

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

	public ResultType getResultType() {
        if (errorCount() > 0)
            return ResultType.ERROR;
        if (failureCount() > 0)
            return ResultType.FAILURE;
        return ResultType.SUCCESS;
	}

	public int failureCount() {
		int failureCount = 0;
		for (TestCaseResult testCaseResult : testCaseResults)
			if (testCaseResult.hadFailure())
				failureCount++;
		return failureCount;
	}

	public int errorCount() {
		int errorCount = 0;
		for (TestCaseResult testCaseResult : testCaseResults)
			if (testCaseResult.hadError())
				errorCount++;
		return errorCount;
	}

	public int count() {
		return testCaseResults.size();
	}

}
