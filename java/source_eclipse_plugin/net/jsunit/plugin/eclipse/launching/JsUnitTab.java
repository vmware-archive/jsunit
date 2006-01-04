package net.jsunit.plugin.eclipse.launching;

import java.util.Iterator;
import java.util.List;

import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

public class JsUnitTab extends AbstractLaunchConfigurationTab {
	
	private Text testPagePathText;
	private Text projectText;
	private Button searchButton;
	private Button projectButton; 
	private final Image testIcon = createImage("jsunitlaunch.gif");
	private Label testPageLabel;
	private Label projectLabel;
	
	public void createControl(Composite parent) {		
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);

		GridLayout topLayout = new GridLayout();
		topLayout.numColumns= 3;
		comp.setLayout(topLayout);		
		
		Label label = new Label(comp, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
		
		createTestPageSection(comp);

		Dialog.applyDialogFont(comp);
		validatePage();
	}
	
	protected void createTestPageSection(Composite comp) {
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		
		projectLabel = new Label(comp, SWT.NONE);
		projectLabel.setText("Project:"); 
		gd= new GridData();
		gd.horizontalIndent = 25;
		projectLabel.setLayoutData(gd);
		
		projectText= new Text(comp, SWT.SINGLE | SWT.BORDER);
		projectText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				validatePage();
				updateLaunchConfigurationDialog();				
				searchButton.setEnabled(projectText.getText().length() > 0);
			}
		});
			
		projectButton = new Button(comp, SWT.PUSH);
		projectButton.setText("Browse..."); 
		projectButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleProjectButtonSelected();
			}
		});
		setButtonGridData(projectButton);
		
		testPageLabel = new Label(comp, SWT.NONE);
		gd = new GridData();
		gd.horizontalIndent = 25;
		testPageLabel.setLayoutData(gd);
		testPageLabel.setText("Test Page:"); 
		
	
		testPagePathText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		testPagePathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		testPagePathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				validatePage();
				updateLaunchConfigurationDialog();
			}
		});
		
		searchButton = new Button(comp, SWT.PUSH);
		searchButton.setEnabled(projectText.getText().length() > 0);		
		searchButton.setText("Search..."); 
		searchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleSearchButtonSelected();
			}
		});
		setButtonGridData(searchButton);
		
	}

	protected static Image createImage(String path) {
		return ImageDescriptor.createFromURL(JsUnitPlugin.iconFileURL(path)).createImage();
	}


	public void initializeFrom(ILaunchConfiguration config) {
		updateProjectFromConfiguration(config);
		updateTestPageFromConfiguration(config);
	}

	protected void updateProjectFromConfiguration(ILaunchConfiguration config) {
		String projectName= "";
		try {
			projectName = config.getAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_PROJECT_NAME, "");
		} catch (CoreException ce) {
		}
		projectText.setText(projectName);
	}
	
	protected void updateTestPageFromConfiguration(ILaunchConfiguration config) {
		String testPagePath= "";
		try {
			testPagePath = config.getAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_TEST_PAGE_PATH, "");
		} catch (CoreException ce) {		
		}
		testPagePathText.setText(testPagePath);
	}

	public void performApply(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_PROJECT_NAME, projectText.getText());
		config.setAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_TEST_PAGE_PATH, testPagePathText.getText());
	}

	public void dispose() {
		super.dispose();
		testIcon.dispose();
	}

	public Image getImage() {
		return testIcon;
	}

	protected void handleSearchButtonSelected() {
		Shell shell = getShell();
		
		IProject project = getProject();
		
		SelectionDialog dialog = new TestPageSelectionDialog(shell, project);
		dialog.setTitle("Test Page selection");
		
		StringBuffer messageBuffer = new StringBuffer();
		messageBuffer.append("Choose a Test Page or Test Page Suite:");
		messageBuffer.append("\nTest Pages are files whose extension is one of ");
		List<String> testPageExtensions = JsUnitPlugin.soleInstance().getJsUnitPreferenceStore().testPageExtensions();
		for (Iterator<String> it = testPageExtensions.iterator(); it.hasNext();) {
			messageBuffer.append(".").append(it.next());
			if (it.hasNext())
				messageBuffer.append(", ");
		}
		messageBuffer.append(".\nYou can change the extension list in Window->Preferences->JsUnit.");

		dialog.setMessage(messageBuffer.toString());
		if (dialog.open() == Window.CANCEL) {
			return;
		}
		
		Object[] results = dialog.getResult();
		if ((results == null) || (results.length < 1)) {
			return;
		}		
		IFile testPage = (IFile) results[0];
		
		if (testPage!= null) {
			testPagePathText.setText(testPage.getProjectRelativePath().toString());
			project = testPage.getProject();
			projectText.setText(project.getName());
		}
	}

	protected void handleProjectButtonSelected() {
		IProject project = chooseProject();
		if (project == null) {
			return;
		}
		projectText.setText(project.getName());		
	}
	
	protected IProject chooseProject() {
		IProject[] projects = getWorkspaceRoot().getProjects();
		
		ILabelProvider labelProvider= new ProjectLabelProvider();
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), labelProvider);
		dialog.setTitle("Project selection"); 
		dialog.setMessage("Choose a project inside of which to constrain the search for Test Pages:"); 
		dialog.setElements(projects);
		
		IProject project = getProject();
		if (project != null) {
			dialog.setInitialSelections(new Object[] { project });
		}
		if (dialog.open() == Window.OK) {			
			return (IProject) dialog.getFirstResult();
		}			
		return null;		
	}
	
	protected IProject getProject() {
		String projectName = projectText.getText().trim();
		if (projectName.length() < 1) {
			return null;
		}
		return getWorkspaceRoot().getProject(projectName);		
	}
	
	private IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	public boolean isValid(ILaunchConfiguration config) {		
		return getErrorMessage() == null;
	}
	
	private void validatePage() {
		setErrorMessage(null);
		setMessage(null);

		String projectName= projectText.getText().trim();
		if (projectName.length() == 0) {
			setErrorMessage("Project not specified");
			return;
		}

		IStatus status= ResourcesPlugin.getWorkspace().validatePath(IPath.SEPARATOR + projectName, IResource.PROJECT);
		if (!status.isOK()) {
			setErrorMessage("Project not specified");
			return;
		}

		IProject project= getWorkspaceRoot().getProject(projectName);
		if (!project.exists()) {
			setErrorMessage("Project does not exist");
			return;
		}

	}

	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_PROJECT_NAME, "");
	}

	public String getName() {
		return "Test Page"; 
	}
		
	protected void setButtonGridData(Button button) {
		GridData gridData= new GridData();
		button.setLayoutData(gridData);
		gridData.horizontalAlignment = GridData.FILL;	 	
	}
	
	static class ProjectLabelProvider extends LabelProvider {
	    public String getText(Object element) {
	    	if (element instanceof IProject)
	    		return ((IProject) element).getName();
	        return super.getText(element);
	    }

	}

}