package net.jsunit.plugin.eclipse.resultsui;

import java.util.HashMap;
import java.util.Map;

import net.jsunit.model.ResultType;
import net.jsunit.model.TestCaseResult;
import net.jsunit.model.TestPageResult;

public class TestPageResultNode extends ParentNode {

	private static Map<ResultType, String> resultTypeToImageName;
	static {
		resultTypeToImageName = new HashMap<ResultType, String>();
		resultTypeToImageName.put(ResultType.ERROR, "testerr.gif");
		resultTypeToImageName.put(ResultType.FAILURE, "testfail.gif");
		resultTypeToImageName.put(ResultType.SUCCESS, "testok.gif");
	}

	private TestPageResult result;

	public TestPageResultNode(TestPageResult result) {
		super(result.getTestPageName());
		this.result = result;
		for (TestCaseResult testCaseResult : result.getTestCaseResults())
			addChild(new TestCaseResultNode(testCaseResult));
	}

	protected String getStatus() {
		return result.getResultType().getDisplayString();
	}

	public String getImageName() {
		return resultTypeToImageName.get(result.getResultType());
	}

}
