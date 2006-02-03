package net.jsunit.plugin.eclipse.resultsui;

import java.util.HashMap;
import java.util.Map;

import net.jsunit.model.BrowserResult;
import net.jsunit.model.ResultType;
import net.jsunit.model.TestPageResult;

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
		for (TestPageResult testPageResult : result.getTestPageResults())
			addChild(new TestPageResultNode(testPageResult));
	}
	
	public String getStatus() {
		if (running)
			return "running...";
		return result == null ? null : result.getDisplayString();
	}

	public void setRunning(boolean b) {
		running = b;
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

}
