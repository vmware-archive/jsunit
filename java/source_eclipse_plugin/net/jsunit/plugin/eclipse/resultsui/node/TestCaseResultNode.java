package net.jsunit.plugin.eclipse.resultsui.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jsunit.model.ResultType;
import net.jsunit.model.TestCaseResult;

public class TestCaseResultNode extends Node {

	private static Map<ResultType, String> resultTypeToImageName;
	static {
		resultTypeToImageName = new HashMap<ResultType, String>();
		resultTypeToImageName.put(ResultType.ERROR, "testerr.gif");
		resultTypeToImageName.put(ResultType.FAILURE, "testfail.gif");
		resultTypeToImageName.put(ResultType.SUCCESS, "testok.gif");
	}
	
	private TestCaseResult result;

	public TestCaseResultNode(TestCaseResult result) {
		super(result.getFullyQualifiedName());
		this.result = result;
	}
	
	public String getProblemSummary() {
		return result.getProblemSummary();
	}

	public boolean wasSuccessful() {
		return result.wasSuccessful();
	}
	
	protected String getStatus() {
		return result.getResultType().getDisplayString();
	}

	public String getImageName() {
		return resultTypeToImageName.get(result.getResultType());
	}

	public TestCaseResult getResult() {
		return result;
	}

	public String getDisplayStringWithBrowserFileName() {
		BrowserResultNode browserNode = (BrowserResultNode) getParent().getParent();
		return browserNode.getName() + ":" + getDisplayString();
	}

	public List<TestCaseResultNode> getProblemTestCaseResultNodes() {
		List<TestCaseResultNode> result = new ArrayList<TestCaseResultNode>();
		if (!wasSuccessful())
			result.add(this);
		return result;
	}

}