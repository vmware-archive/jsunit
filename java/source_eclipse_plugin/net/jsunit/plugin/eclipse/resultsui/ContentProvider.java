package net.jsunit.plugin.eclipse.resultsui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.jsunit.TestRunListener;
import net.jsunit.model.BrowserResult;
import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

class ContentProvider implements IStructuredContentProvider, ITreeContentProvider, TestRunListener {
	
	private IViewSite viewSite;

	ContentProvider(IViewSite site) {
		this.viewSite = site;
	}
	
	private InvisibleRootNode invisibleRoot;
	private int testFailureCount;
	private int testErrorCount;
	private int testCount;

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
	
	public void dispose() {
	}
	
	public Object[] getElements(Object parent) {
		if (parent.equals(viewSite)) {
			if (invisibleRoot == null)
				initialize();
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}
	
	public Object getParent(Object child) {
		if (child instanceof Node) {
			return ((Node) child).getParent();
		}
		return null;
	}
	
	public Object [] getChildren(Object parent) {
		if (parent instanceof ParentNode) {
			List<Node> childrenList = ((ParentNode)parent).getChildren();
			return childrenList.toArray(new Node[childrenList.size()]);
		}
		return new Object[0];
	}
	
	public boolean hasChildren(Object parent) {
		if (parent instanceof ParentNode)
			return ((ParentNode)parent).hasChildren();
		return false;
	}

	private void initialize() {
		testFailureCount = 0;
		testErrorCount = 0;
		testCount = 0;
		invisibleRoot = new InvisibleRootNode();
	}

	public void reset() {
	}
	
	public List<BrowserResultNode> getBrowserResultNodes() {
		return invisibleRoot.getBrowserResultChildrenNodes();
	}

	private BrowserResultNode findBrowserNode(String browserFileName) {
		for (Iterator it = getBrowserResultNodes().iterator(); it.hasNext();) {
			BrowserResultNode browserNode = (BrowserResultNode) it.next();
			if (browserNode.getName().equals(browserFileName)) {
				return browserNode;
			}
		}
		return null;
	}

	public int getCompletedBrowserTestRunCount() {
		int result = 0;
		for (Iterator it = getBrowserResultNodes().iterator(); it.hasNext();) {
			BrowserResultNode browserNode = (BrowserResultNode) it.next();
			if (browserNode.hasCompletedTestRun())
				result++;
		}
		return result;
	}

	public int getTestFailureCount() {
		return testFailureCount;
	}

	public int getTestErrorCount() {
		return testErrorCount;
	}
	
	public int getCompletedTestCount() {
		return testCount;
	}

	public List<TestCaseResultNode> getProblemTestCaseResultNodes() {
		List<TestCaseResultNode> result = new ArrayList<TestCaseResultNode>();
		for (BrowserResultNode browserNode : getBrowserResultNodes())
			result.addAll(browserNode.getProblemTestCaseResultNodes());
		return result;
	}

	public boolean isReady() {
		return true;
	}

	public void testRunStarted() {
		initialize();
		List<String> browserFileNames = JsUnitPlugin.soleInstance().getJsUnitPreferenceStore().browserFileNames();
		for (String browser : browserFileNames)
			invisibleRoot.addChild(new BrowserResultNode(browser));
	}

	public void testRunFinished() {
		ensureAllBrowserNodesAreNotRunning();
	}
	
	private void ensureAllBrowserNodesAreNotRunning() {
		for (BrowserResultNode node : getBrowserResultNodes())
			node.setRunning(false);
	}

	public void browserTestRunStarted(String browserFileName) {
		BrowserResultNode node = findBrowserNode(browserFileName);
		if (node != null)
			node.setRunning(true);
	}

	public void browserTestRunFinished(String browserFileName, BrowserResult result) {
		BrowserResultNode node = findBrowserNode(browserFileName);
		if (node != null)
			node.setResult(result);
		testCount += result.count();
		testErrorCount += result.errorCount();
		testFailureCount += result.failureCount();
	}

	public void expandAll() {
		
	}

}
