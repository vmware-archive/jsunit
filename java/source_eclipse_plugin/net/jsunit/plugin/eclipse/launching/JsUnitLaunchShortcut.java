package net.jsunit.plugin.eclipse.launching;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

public class JsUnitLaunchShortcut implements ILaunchShortcut {

	private ILaunchManager launchManager;

	public JsUnitLaunchShortcut() {
		launchManager = DebugPlugin.getDefault().getLaunchManager();
	}
	
	public void launch(ISelection selection, String mode) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		IFile file = (IFile) structuredSelection.getFirstElement();
		ILaunchConfiguration config = createConfiguration(file);
		DebugUITools.launch(config, mode);
	}

	public void launch(IEditorPart editor, String mode) {
		throw new RuntimeException("not implemented");
	}

	private ILaunchConfiguration createConfiguration(IFile file) {
		ILaunchConfiguration config = null;
		try {
			ILaunchConfigurationType configType = jsUnitLaunchConfigType();
			String testPagePath = file.getProjectRelativePath().toString();
			ILaunchConfigurationWorkingCopy wc = configType.newInstance(
				null, launchManager.generateUniqueLaunchConfigurationNameFrom(testPagePath)
			);
			wc.setAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_TEST_PAGE_PATH, testPagePath);
			wc.setAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_PROJECT_NAME, file.getProject().getName());
			config = wc.doSave();		
		} catch (CoreException ce) {
			throw new RuntimeException(ce);
		}
		return config;
	}

	private ILaunchConfigurationType jsUnitLaunchConfigType() {
		return launchManager.getLaunchConfigurationType(JsUnitLaunchConfiguration.ID_JSUNIT_APPLICATION);		
	}

}