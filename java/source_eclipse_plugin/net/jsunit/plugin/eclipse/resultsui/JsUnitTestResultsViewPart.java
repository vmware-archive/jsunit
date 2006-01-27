package net.jsunit.plugin.eclipse.resultsui;

import java.util.ArrayList;
import java.util.List;

import net.jsunit.RemoteTestRunClient;
import net.jsunit.TestRunListener;
import net.jsunit.model.BrowserResult;
import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class JsUnitTestResultsViewPart extends ViewPart implements TestRunListener {
	
	static public final String ID = "net.jsunit.plugin.eclipse.resultsui.JsUnitTestResultsViewPart";

	private JsUnitProgressBar progressBar;
	private ContentProvider contentProvider;
	private Composite counterComposite;
	private CounterPanel counterPanel;
	private SashForm sashForm;
	private CTabFolder tabFolder;
	private FailureTrace failureTrace;
	private Clipboard clipboard;
	private List<TestResultsTab> testResultsTabs;
	private TestResultsTab activeTab;
	private StopAction stopAction;
	private RemoteTestRunClient client;

	public void createPartControl(Composite parent) {
		contentProvider = new ContentProvider(getViewSite());
		GridLayout gridLayout= new GridLayout(); 
		gridLayout.marginWidth= 0;
		gridLayout.marginHeight= 0;
		parent.setLayout(gridLayout);

		counterComposite = createProgressCountPanel(parent);
		counterComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		SashForm sashForm = createSashForm(parent);
		sashForm.setOrientation(SWT.HORIZONTAL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		stopAction = new StopAction();
		configureToolBar();
	}
	
	private SashForm createSashForm(Composite parent) {
		clipboard = new Clipboard(parent.getDisplay());

		sashForm = new SashForm(parent, SWT.VERTICAL);
		
		ViewForm left = new ViewForm(sashForm, SWT.NONE);

		ViewForm right = new ViewForm(sashForm, SWT.NONE);
		CLabel label= new CLabel(right, SWT.NONE);
		label.setText("Failures");
		label.setImage(JsUnitPlugin.createImage("stkfrm_obj.gif"));
		right.setTopLeft(label);

		ToolBar failureToolBar = new ToolBar(right, SWT.FLAT | SWT.WRAP);
		right.setTopCenter(failureToolBar);
		failureTrace= new FailureTrace(right, clipboard, this, failureToolBar);
		right.setContent(failureTrace.getComposite()); 
		
		tabFolder = createTestRunTabs(left);
		tabFolder.setLayoutData(new TabFolderLayout());
		left.setContent(tabFolder);

		sashForm.setWeights(new int[]{50, 50});
		return sashForm;
	}
	
	protected CTabFolder createTestRunTabs(Composite parent) {
		CTabFolder tabFolder= new CTabFolder(parent, SWT.TOP);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL));
		testResultsTabs = new ArrayList<TestResultsTab>();

		testResultsTabs.add(new AllTestsByBrowserResultsTab(tabFolder, getViewSite(), contentProvider, failureTrace));
//		testResultsTabs.add(new AllTestsByTestResultsTab(tabFolder, getViewSite(), contentProvider, failureTrace));
		testResultsTabs.add(new FailuresTestResultsTab(tabFolder, getViewSite(), contentProvider, failureTrace));
		
		tabFolder.setSelection(0);				
		activeTab = (TestResultsTab) testResultsTabs.get(0);		
				
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				testResultsTabChanged(event);
			}
		});
		return tabFolder;
	}
	
	private void testResultsTabChanged(SelectionEvent event) {
		for (TestResultsTab tab : testResultsTabs) {
			if (((CTabFolder) event.widget).getSelection().getText() == tab.getName())
				activeTab= tab;
				activeTab.refresh();
		}
	}
	
	protected Composite createProgressCountPanel(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		GridLayout layout= new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;
		counterPanel = new CounterPanel(composite);
		counterPanel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		progressBar = new JsUnitProgressBar(composite);
		progressBar.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		return composite;
	}
	
	public void setFocus() {
		activeTab.setFocus();
	}

	private void refreshActiveTab() {
		activeTab.refresh();
	}

	public void reset() {
		contentProvider.reset();
		resetBrowserCountAndProgressBar();
		refreshActiveTab();
	}

	private void resetBrowserCountAndProgressBar() {
		final int browserCount = contentProvider.getBrowserResultNodes().size();
		JsUnitPlugin.getDisplay().asyncExec(new Runnable() {
			public void run() {
				progressBar.reset();
				counterPanel.reset();
				progressBar.setMaximum(browserCount);
				counterPanel.setTotalBrowserCount(browserCount);
			}
		});
	}
	
	private void configureToolBar() {
		IActionBars actionBars= getViewSite().getActionBars();
		IToolBarManager toolBar= actionBars.getToolBarManager();
		stopAction.setEnabled(false);
		toolBar.add(stopAction);
		actionBars.updateActionBars();
	}
	
	private class StopAction extends Action {
		public StopAction() {
			setText("Stop");
			setToolTipText("Stop current test run");
			setDisabledImageDescriptor(JsUnitPlugin.createImageDescriptor("stopdisabled.gif"));
			setHoverImageDescriptor(JsUnitPlugin.createImageDescriptor("stopenabled.gif"));
			setImageDescriptor(JsUnitPlugin.createImageDescriptor("stopenabled.gif"));
		}

		public void run() {
			hitServer("stop");
			setEnabled(false);
		}

		private void hitServer(String string) {
			//TODO
			
		}
	}

	public boolean isReady() {
		return true;
	}

	public void testRunStarted() {
		contentProvider.testRunStarted();
		resetBrowserCountAndProgressBar();
		refreshActiveTab();
}

	public void testRunFinished() {
		contentProvider.testRunFinished();
	}

	public void browserTestRunFinished(String browserFileName, BrowserResult result) {
		contentProvider.browserTestRunFinished(browserFileName, result);
		JsUnitPlugin.getDisplay().asyncExec(new Runnable() {
			public void run() {
				progressBar.step(contentProvider.getTestFailureCount() + contentProvider.getTestErrorCount());
				counterPanel.setBrowserRunCount(contentProvider.getCompletedBrowserTestRunCount());
				counterPanel.setTestRunCount(contentProvider.getCompletedTestCount());
				counterPanel.setTestErrorCount(contentProvider.getTestErrorCount());
				counterPanel.setTestFailureCount(contentProvider.getTestFailureCount());
			}
		});
		stopAction.setEnabled(false);
		refreshActiveTab();
	}

	public void browserTestRunStarted(String browserFileName) {
		contentProvider.browserTestRunStarted(browserFileName);
		refreshActiveTab();
		stopAction.setEnabled(true);
	}

	public void connectToRemoteRunner(int serverPort) {
		client = new RemoteTestRunClient(this, serverPort);
		client.startListening();
	}

}