package net.jsunit.plugin.eclipse.launching;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

public class JsUnitLaunchConfiguration implements ILaunchConfigurationDelegate {

	public static final String ID_JSUNIT_APPLICATION = "net.jsunit.plugin.eclipse.launchConfiguration";
	public static final String ATTRIBUTE_TEST_PAGE_PATH = "testPagePath";
	public static final String ATTRIBUTE_PROJECT_NAME = "projectName";

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		String projectName = configuration.getAttribute(ATTRIBUTE_PROJECT_NAME, (String) null);
		String fileRelativePath = configuration.getAttribute(ATTRIBUTE_TEST_PAGE_PATH, (String) null);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		IFile testPage = project.getFile(fileRelativePath);
		new JsUnitLauncher().launch(testPage);
	}
	
}