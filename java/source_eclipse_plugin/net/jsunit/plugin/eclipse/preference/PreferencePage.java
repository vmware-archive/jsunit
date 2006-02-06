package net.jsunit.plugin.eclipse.preference;

import java.io.File;

import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	private DirectoryFieldEditor logsDirectory;
	private Composite logsDirectoryParent;
	private BooleanFieldEditor useDefaultLogsDirectory;
	private DirectoryFieldEditor installationDirectory;
	
	public PreferencePage() {
		super(
			"JsUnit configuration", 
			ImageDescriptor.createFromURL(JsUnitPlugin.iconFileURL("jsunitlaunch.gif")),
			GRID
		);
		setPreferenceStore(JsUnitPlugin.soleInstance().getPreferenceStore());
	}
	
	public void createFieldEditors() {
		Composite testPageExtensionParent = getFieldEditorParent();
		createSpacer(testPageExtensionParent, 3);
		
		installationDirectory = new DirectoryFieldEditor(
						PreferenceConstants.PREFERENCE_INSTALLATION_DIRECTORY, 
						"&JsUnit installation directory:", 
						testPageExtensionParent);
		addField(installationDirectory);

		useDefaultLogsDirectory = new BooleanFieldEditor(
						PreferenceConstants.PREFERENCE_USE_DEFAULT_LOGS_DIRECTORY, 
						"&Use default logs directory (i.e. <installation directory>/logs)", 
						testPageExtensionParent);
		addField(useDefaultLogsDirectory);
		
		logsDirectoryParent = testPageExtensionParent;
		logsDirectory = new DirectoryFieldEditor(
						PreferenceConstants.PREFERENCE_LOGS_DIRECTORY, 
						"&Custom logs directory:", 
						logsDirectoryParent);
		addField(logsDirectory);
		
		createSpacer(testPageExtensionParent, 3);
		
		addField(new ListFileFieldEditor(
				PreferenceConstants.PREFERENCE_BROWSER_FILE_NAMES,
				"&Browser executables (in execution order):",
				testPageExtensionParent
		));
		
		createSpacer(testPageExtensionParent, 3);
		
		addField(new StringFieldEditor(
					PreferenceConstants.PREFERENCE_TEST_PAGE_EXTENSIONS,
					"&Test Page extensions:",
					testPageExtensionParent));
		
		createFieldHelpText(testPageExtensionParent, 1, 2, "Comma-separated, e.g. \"html,htm\"");
		createSpacer(testPageExtensionParent, 3);

		addField(
				new BooleanFieldEditor(
					PreferenceConstants.PREFERENCE_CLOSE_BROWSERS_AFTER_TEST_RUNS,
					"&Attempt to close browsers after test runs",
					testPageExtensionParent));

		createSpacer(testPageExtensionParent, 3);

		addField(new IntegerFieldEditor(
					PreferenceConstants.PREFERENCE_TIMEOUT_SECONDS,
					"&Seconds to wait before timing out browsers:",
					testPageExtensionParent));
		
	}
	
	protected void createSpacer(Composite composite, int columnSpan) {
		Label label = new Label(composite, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalSpan = columnSpan;
		label.setLayoutData(gd);
	}
	
	protected void createFieldHelpText(Composite composite, int columnsBefore, int columnSpan, String text) {
		Label spacerLabel = new Label(composite, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalSpan = columnsBefore;
		spacerLabel.setLayoutData(gd);
		
		Label textLabel = new Label(composite, SWT.NONE);
		textLabel.setText(text);
		gd = new GridData();
		gd.horizontalSpan = columnSpan;
		textLabel.setLayoutData(gd);
	}	

	public void init(IWorkbench workbench) {
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() == useDefaultLogsDirectory) {
			boolean isChecked = (Boolean) event.getNewValue();
			updateEnabledStateOfLogsDirectory(isChecked);
		} else if (event.getSource() == installationDirectory && useDefaultLogsDirectory.getBooleanValue()) {
			logsDirectory.setStringValue(event.getNewValue() + File.separator + "logs");
		} else {
			super.propertyChange(event);
		}
	}
	
	protected void initialize() {
		super.initialize();
		updateEnabledStateOfLogsDirectory();
	}

	protected void performDefaults() {
		super.performDefaults();
		updateEnabledStateOfLogsDirectory();
		
	}

	private void updateEnabledStateOfLogsDirectory() {
		boolean isUseDefaultLogsDirectoryChecked = useDefaultLogsDirectory.getBooleanValue();
		updateEnabledStateOfLogsDirectory(isUseDefaultLogsDirectoryChecked);
	}
	
	private void updateEnabledStateOfLogsDirectory(boolean isUseDefaultLogsDirectoryChecked) {
		logsDirectory.setEnabled(!isUseDefaultLogsDirectoryChecked, logsDirectoryParent);
	}

}