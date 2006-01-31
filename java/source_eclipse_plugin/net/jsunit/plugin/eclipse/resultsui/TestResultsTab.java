package net.jsunit.plugin.eclipse.resultsui;

import net.jsunit.model.TestCaseResult;
import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;

public abstract class TestResultsTab {
	
	private FailureTrace failureTrace;

	public TestResultsTab(CTabFolder tabFolder, IViewSite viewSite, ContentProvider contentProvider, FailureTrace failureTrace) {
		this.failureTrace = failureTrace;
		CTabItem tab= new CTabItem(tabFolder, SWT.NONE);
		tab.setText(getName());
		tab.setImage(JsUnitPlugin.createImage(getImageName()));

		Composite panel= new Composite(tabFolder, SWT.NONE);
		GridLayout gridLayout= new GridLayout();
		gridLayout.marginHeight= 0;
		gridLayout.marginWidth= 0;
		panel.setLayout(gridLayout);
		
		GridData gridData= new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		panel.setLayoutData(gridData);
		
		tab.setControl(panel);
		tab.setToolTipText(getToolTipText()); 
		
		addControlToPanel(viewSite, contentProvider, panel);	

		hookSelectionChangedEventTo(createSelectionChangedAction());

	}

	protected abstract String getImageName();
	
	private Action createSelectionChangedAction() {
		return new Action() {
			public void run() {
				Object selection = getSelectedTestCaseResult();
				if (selection instanceof TestCaseResult) {
					TestCaseResult result = (TestCaseResult) selection;
					failureTrace.showFailure(result);
				} else {
					failureTrace.clear();
				}
			}

		};
	}
	
	protected abstract void hookSelectionChangedEventTo(Action action);
	
	protected abstract void addControlToPanel(IViewSite viewSite, ContentProvider contentProvider, Composite panel);

	protected abstract String getToolTipText();
	
	protected abstract TestCaseResult getSelectedTestCaseResult();

	public abstract void setFocus();
	
	public abstract String getName();

	public abstract void refresh();

	public abstract void expandAll();	

	public abstract void collapseAll();

	public abstract boolean isHierarchical();
	
}