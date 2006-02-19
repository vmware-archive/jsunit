package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.resultsui.ContentProvider;
import net.jsunit.plugin.eclipse.resultsui.NodeLabelProvider;
import net.jsunit.plugin.eclipse.resultsui.TestResultsTab;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;

public class MockTestResultsTab extends TestResultsTab {

	public boolean wasExpandAllCalled;
	public boolean wasCollapseAllCalled;
	public boolean wasRefreshCalled;

	public MockTestResultsTab() {
		super(null, null, null, null, null);
	}
	
	protected void createCTabItem(CTabFolder tabFolder, IViewSite viewSite, ContentProvider contentProvider, NodeLabelProvider labelProvider) {
	}

	protected String getImageName() {
		return null;
	}

	protected void hookSelectionChangedEventTo(Action action) {
	}

	protected void addControlToPanel(IViewSite viewSite, ContentProvider contentProvider, Composite panel, NodeLabelProvider labelProvider) {
	}

	protected String getToolTipText() {
		return null;
	}

	protected Object getSelectedData() {
		return null;
	}

	public void setFocus() {
	}

	public String getName() {
		return null;
	}

	public void refresh() {
		wasRefreshCalled = true;
	}

	public void expandAll() {
		wasExpandAllCalled = true;
	}

	public void collapseAll() {
		wasCollapseAllCalled = true;
	}

	public boolean isHierarchical() {
		return false;
	}

}
