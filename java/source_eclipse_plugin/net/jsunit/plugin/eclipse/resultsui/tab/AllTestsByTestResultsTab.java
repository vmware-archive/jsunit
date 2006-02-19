package net.jsunit.plugin.eclipse.resultsui.tab;

import net.jsunit.plugin.eclipse.resultsui.ContentProvider;
import net.jsunit.plugin.eclipse.resultsui.FailureTrace;
import net.jsunit.plugin.eclipse.resultsui.NodeLabelProvider;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.ui.IViewSite;

public class AllTestsByTestResultsTab extends HierarchyTestResultsTab {

	public AllTestsByTestResultsTab(CTabFolder tabFolder, IViewSite viewSite, ContentProvider contentProvider, FailureTrace failureTrace, NodeLabelProvider labelProvider) {
		super(tabFolder, viewSite, contentProvider, failureTrace, labelProvider);
	}

	protected String getToolTipText() {
		return "Shows all test results, arranged by test";
	}

	public String getName() {
		return "Test Hierarchy";
	}
	
	protected String getImageName() {
		return "tsuite.gif";
	}

}