package net.jsunit.plugin.eclipse.resultsui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;

public class FailuresTestResultsTab extends TestResultsTab {

	private FailuresTable table;

	public FailuresTestResultsTab(CTabFolder tabFolder, IViewSite viewSite, ContentProvider contentProvider, FailureTrace failureTrace) {
		super(tabFolder, viewSite, contentProvider, failureTrace);
	}

	protected void addControlToPanel(IViewSite viewSite, ContentProvider contentProvider, Composite panel) {
		table = new FailuresTable(panel, SWT.NONE, contentProvider);
		GridLayout gridLayout= new GridLayout();
		gridLayout.marginHeight= 0;
		gridLayout.marginWidth= 0;
		table.setLayout(gridLayout);
		GridData gridData= new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		table.setLayoutData(gridData);
	}
	
	protected void hookSelectionChangedEventTo(final Action action) {
		OpenStrategy handler = new OpenStrategy(table);
		handler.addPostSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				action.run();
			}
		});

	}

	protected String getToolTipText() {
		return "Shows only tests that failed or had an error";
	}

	public void setFocus() {
	}

	public String getName() {
		return "Failures";
	}

	public void refresh() {
		table.refresh();
	}
	
	protected Object getSelectedData() {
		return table.getSelection()[0].getData();
	}

	protected String getImageName() {
		return "failures.gif";
	}

	public void expandAll() {
	}

	public void collapseAll() {
	}

	public boolean isHierarchical() {
		return false;
	}

}