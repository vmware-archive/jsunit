package net.jsunit.plugin.eclipse.resultsui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jsunit.model.BrowserResult;
import net.jsunit.model.ResultType;
import net.jsunit.model.TestCaseResult;

public class BrowserResultNode extends ParentNode {

	private static Map<ResultType, String> resultTypeToImageName;
	static {
		resultTypeToImageName = new HashMap<ResultType, String>();
		resultTypeToImageName.put(ResultType.ERROR, "tsuiteerror.gif");
		resultTypeToImageName.put(ResultType.FAILURE, "tsuitefail.gif");
		resultTypeToImageName.put(ResultType.SUCCESS, "tsuiteok.gif");
	}
	
	private BrowserResult result;
	private boolean running;

	public BrowserResultNode(String browserFileName) {
		super(browserFileName);
	}
	
	public void setResult(BrowserResult result) {
		this.result = result;
		this.running = false;
		for (TestCaseResult testCaseResult : result.getTestCaseResults())
			addChild(new TestCaseResultNode(testCaseResult));
	}
	
	public String getStatus() {
		if (running)
			return "running...";
		return result == null ? null : result.getResultType().getDisplayString();
	}

	public void setRunning() {
		running = true;		
	}
	
	public String getImageName() {
		if (running)
			return "tsuiterun.gif";
		if (result == null)
			return "tsuite.gif";
		return resultTypeToImageName.get(result.getResultType());
	}

	public boolean hasCompletedTestRun() {
		return result != null;
	}

	public List<TestCaseResultNode> getTestCaseChildrenNodes() {
		List<TestCaseResultNode> result = new ArrayList<TestCaseResultNode>();
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			result.add((TestCaseResultNode) it.next());
		}
		return result;
	}

}
