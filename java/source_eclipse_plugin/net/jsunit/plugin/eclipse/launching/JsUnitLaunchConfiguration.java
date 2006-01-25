package net.jsunit.plugin.eclipse.launching;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.jsunit.configuration.Configuration;
import net.jsunit.plugin.eclipse.JsUnitPlugin;
import net.jsunit.plugin.eclipse.preference.PreferenceConfigurationSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
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

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		IVMInstall install= getVMInstall(configuration);
		IVMRunner runner = install.getVMRunner(mode);
		if (runner == null)
			throw new RuntimeException("No VM runner");
		int port= SocketUtil.findFreePort();
		VMRunnerConfiguration runConfig = createVMRunnerConfiguration(configuration, mode, port);
		runner.run(runConfig, launch, monitor);
	}
	
	private VMRunnerConfiguration createVMRunnerConfiguration(ILaunchConfiguration configuration, String mode, int port) throws CoreException {
		File workingDir = verifyWorkingDirectory(configuration);
		String workingDirName = null;
		if (workingDir != null) 
			workingDirName = workingDir.getAbsolutePath();
		
		String vmArgs = getVMArguments(configuration);
		ExecutionArguments execArgs = new ExecutionArguments(vmArgs, "");
		String[] envp = DebugPlugin.getDefault().getLaunchManager().getEnvironment(configuration);

		VMRunnerConfiguration runConfig = createVMRunner(configuration, port, mode);
		runConfig.setVMArguments(execArgs.getVMArgumentsArray());
		runConfig.setWorkingDirectory(workingDirName);
		runConfig.setEnvironment(envp);

		Map vmAttributesMap = getVMSpecificAttributesMap(configuration);
		runConfig.setVMSpecificAttributesMap(vmAttributesMap);

		String[] bootpath = getBootpath(configuration);
		runConfig.setBootClassPath(bootpath);

		return runConfig;
	}
	
	private VMRunnerConfiguration createVMRunner(ILaunchConfiguration configuration, int port, String runMode) throws CoreException {
		String[] classPath = createClassPath(configuration);	
		String projectName = configuration.getAttribute(ATTRIBUTE_PROJECT_NAME, (String) null);
		String fileRelativePath = configuration.getAttribute(ATTRIBUTE_TEST_PAGE_PATH, (String) null);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		IFile testPage = project.getFile(fileRelativePath);
		
		PreferenceConfigurationSource source = JsUnitPlugin.soleInstance().getJsUnitPreferenceStore().asConfigurationSource(testPage);
		String[] args = new Configuration(source).asArgumentsArray();

		VMRunnerConfiguration vmConfig = new VMRunnerConfiguration("net.jsunit.TestRunManager", classPath);
		
		vmConfig.setProgramArguments(args);
		return vmConfig;
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

		if (Platform.inDevelopmentMode()) {
			URL url= Platform.getBundle(JsUnitPlugin.PLUGIN_ID).getEntry("/");
			try {
				classPathEntries.add(Platform.asLocalURL(new URL(url, "java/bin")).getFile());
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			File binDirectory = new File(installationDirectoryString+File.separator+"java"+File.separator+"bin");
			File jsUnitJar = new File(binDirectory, "jsunit.jar");
			classPathEntries.add(jsUnitJar.getAbsolutePath());
		}
		
		File configDirectory = new File(installationDirectoryString+File.separator+"java"+File.separator+"config");
		classPathEntries.add(configDirectory.getAbsolutePath());
		
		String[] result = new String[classPathEntries.size()];
		return classPathEntries.toArray(result);
	}

}