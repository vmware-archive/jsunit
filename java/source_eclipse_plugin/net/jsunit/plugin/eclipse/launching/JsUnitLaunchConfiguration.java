package net.jsunit.plugin.eclipse.launching;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.jsunit.configuration.Configuration;
import net.jsunit.plugin.eclipse.JsUnitPlugin;
import net.jsunit.plugin.eclipse.preference.JsUnitPreferenceStore;
import net.jsunit.plugin.eclipse.preference.PreferenceConfigurationSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.SocketUtil;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

public class JsUnitLaunchConfiguration extends AbstractJavaLaunchConfigurationDelegate {

	public static final String ID_JSUNIT_APPLICATION = "net.jsunit.plugin.eclipse.launchConfiguration";
	public static final String ATTRIBUTE_TEST_PAGE_PATH = "testPagePath";
	public static final String ATTRIBUTE_PROJECT_NAME = "projectName";
	public static final String ATTRIBUTE_REMOTE_NOTIFIER_SERVER_PORT = "port";

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		IVMInstall install= getVMInstall(configuration);
		IVMRunner runner = install.getVMRunner(mode);
		if (runner == null)
			throw new RuntimeException("No VM runner");
		VMRunnerConfiguration runConfig = createVMRunnerConfiguration(configuration, mode);
		runner.run(runConfig, launch, monitor);
	}
	
	private VMRunnerConfiguration createVMRunnerConfiguration(ILaunchConfiguration configuration, String mode) throws CoreException {
		File workingDir = verifyWorkingDirectory(configuration);
		String workingDirName = null;
		if (workingDir != null) 
			workingDirName = workingDir.getAbsolutePath();
		
		String vmArgs = getVMArguments(configuration);
		ExecutionArguments execArgs = new ExecutionArguments(vmArgs, "");
		String[] envp = DebugPlugin.getDefault().getLaunchManager().getEnvironment(configuration);

		VMRunnerConfiguration runConfig = createVMRunner(configuration, mode);
		runConfig.setVMArguments(execArgs.getVMArgumentsArray());
		runConfig.setWorkingDirectory(workingDirName);
		runConfig.setEnvironment(envp);

		Map vmAttributesMap = getVMSpecificAttributesMap(configuration);
		runConfig.setVMSpecificAttributesMap(vmAttributesMap);

		String[] bootpath = getBootpath(configuration);
		runConfig.setBootClassPath(bootpath);

		return runConfig;
	}
	
	private VMRunnerConfiguration createVMRunner(ILaunchConfiguration configuration, String runMode) throws CoreException {
		String[] classPath = createClassPath(configuration);	
		String projectName = configuration.getAttribute(ATTRIBUTE_PROJECT_NAME, (String) null);
		String fileRelativePath = configuration.getAttribute(ATTRIBUTE_TEST_PAGE_PATH, (String) null);
		int remoteNotifierServerPort = configuration.getAttribute(ATTRIBUTE_REMOTE_NOTIFIER_SERVER_PORT, -1);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		IFile testPage = project.getFile(fileRelativePath);
		
		int jsUnitServerPort = findFreePortThatIsNot(remoteNotifierServerPort);
		
		JsUnitPreferenceStore preferenceStore = JsUnitPlugin.soleInstance().getJsUnitPreferenceStore();
		PreferenceConfigurationSource source = preferenceStore.asConfigurationSource(testPage, jsUnitServerPort);
		String[] configurationArguments = new Configuration(source).asArgumentsArray();
		String[] arguments = new String[configurationArguments.length + 1];
		System.arraycopy(configurationArguments, 0, arguments, 0, configurationArguments.length);
		arguments[arguments.length - 1] = String.valueOf(remoteNotifierServerPort);

		VMRunnerConfiguration vmConfig = new VMRunnerConfiguration("net.jsunit.TestRunManager", classPath);
		
		vmConfig.setProgramArguments(arguments);
		return vmConfig;
	}
	
	private int findFreePortThatIsNot(int badPort) {
		int freePort = 0;
		while (freePort == 0 || freePort == badPort)
			freePort = SocketUtil.findFreePort();
		return freePort;
	}

	private String[] createClassPath(ILaunchConfiguration configuration) throws CoreException {
		List<String> classPathEntries = new ArrayList<String>();
		String installationDirectoryString = JsUnitPlugin.soleInstance().getJsUnitPreferenceStore().installationDirectory();
		File libDirectory = new File(installationDirectoryString+File.separator+"java"+File.separator+"lib");
		File[] libJars = libDirectory.listFiles(new FilenameFilter() {

			public boolean accept(File directory, String filename) {
				return filename.toLowerCase().endsWith(".jar");
			}
			
		});
		for (File libJar : libJars)
			classPathEntries.add(libJar.getAbsolutePath());

		File binDirectory = new File(installationDirectoryString+File.separator+"java"+File.separator+"bin");
		File jsUnitJar = new File(binDirectory, "jsunit.jar");
		classPathEntries.add(jsUnitJar.getAbsolutePath());
		
		File configDirectory = new File(installationDirectoryString+File.separator+"java"+File.separator+"config");
		classPathEntries.add(configDirectory.getAbsolutePath());
		
		String[] result = new String[classPathEntries.size()];
		return classPathEntries.toArray(result);
	}

}