package net.jsunit.plugin.eclipse.resultsui.tab;

import net.jsunit.plugin.eclipse.JsUnitPlugin;
import net.jsunit.plugin.eclipse.resultsui.ContentProvider;
import net.jsunit.plugin.eclipse.resultsui.FailureTrace;
import net.jsunit.plugin.eclipse.resultsui.TestResultsTab;
import net.jsunit.plugin.eclipse.resultsui.node.BrowserResultNode;
import net.jsunit.plugin.eclipse.resultsui.node.Node;
import net.jsunit.plugin.eclipse.resultsui.node.TestCaseResultNode;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;

public abstract class HierarchyTestResultsTab extends TestResultsTab {

	private TreeViewer treeViewer;
	
	public HierarchyTestResultsTab(CTabFolder tabFolder, IViewSite viewSite, ContentProvider contentProvider, FailureTrace failureTrace) {
		super(tabFolder, viewSite, contentProvider, failureTrace);
	}
	
	protected void addControlToPanel(IViewSite viewSite, ContentProvider contentProvider, Composite panel) {
		treeViewer = new TreeViewer(panel, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gridData= new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		treeViewer.getControl().setLayoutData(gridData);

		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new ViewLabelProvider());
		treeViewer.setSorter(null);
		treeViewer.setInput(viewSite);
	}

	public void refresh() {
		JsUnitPlugin.getDisplay().syncExec(new Runnable() {
			public void run() {
				treeViewer.refresh();
			}
		});		
	}

	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	protected void hookSelectionChangedEventTo(final Action action) {
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				action.run();
			}
		});		
	}

	protected Object getSelectedData() {
		IStructuredSelection selectionList = (IStructuredSelection) treeViewer.getSelection();
		Node node = (Node) selectionList.getFirstElement();
		if (node instanceof TestCaseResultNode)
			return ((TestCaseResultNode) node).getResult();
		else if (node instanceof BrowserResultNode)
			return ((BrowserResultNode) node).getResult();
		return null;
	}
	
	public void expandAll() {
		treeViewer.expandAll();
	}
	
	public void collapseAll() {
		treeViewer.collapseAll();
	}
	
	public boolean isHierarchical() {
		return true;
	}


}
