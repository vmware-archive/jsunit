package net.jsunit.plugin.eclipse.resultsui.tab;

import net.jsunit.plugin.eclipse.resultsui.ContentProvider;
import net.jsunit.plugin.eclipse.resultsui.FailureTrace;
import net.jsunit.plugin.eclipse.resultsui.NodeLabelProvider;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.ui.IViewSite;

public class AllTestsByBrowserResultsTab extends HierarchyTestResultsTab {

	public AllTestsByBrowserResultsTab(CTabFolder tabFolder, IViewSite viewSite, ContentProvider contentProvider, FailureTrace failureTrace, NodeLabelProvider labelProvider) {
		super(tabFolder, viewSite, contentProvider, failureTrace, labelProvider);
	}

	protected String getToolTipText() {
		return "Shows all test results, arranged by browser";
	}

	public String getName() {
		return "Browser Hierarchy";
	}

	protected String getImageName() {
		return "testhier.gif";
	}

}