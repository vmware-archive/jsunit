package net.jsunit.plugin.eclipse.resultsui;

import net.jsunit.model.TestCaseResult;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;

public class AllTestsByTestResultsTab extends TestResultsTab {

	public AllTestsByTestResultsTab(CTabFolder tabFolder, IViewSite viewSite, ContentProvider contentProvider, FailureTrace failureTrace) {
		super(tabFolder, viewSite, contentProvider, failureTrace);
	}

	protected String getImageName() {
		return "tsuite.gif";
	}

	protected TestCaseResult getSelectedTestCaseResult() {
		return null;
	}

	protected void hookSelectionChangedEventTo(Action action) {
	}

	protected void addControlToPanel(IViewSite viewSite, ContentProvider contentProvider, Composite panel) {
	}

	protected String getToolTipText() {
		return "Shows all test results, arranged by test";
	}

	public void setFocus() {
	}

	public String getName() {
		return "All tests by test";
	}

	public void refresh() {
	}

}