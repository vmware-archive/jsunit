package net.jsunit.plugin.eclipse.preference;

import java.util.List;

import net.jsunit.configuration.ConfigurationSource;

import org.eclipse.core.resources.IFile;

public class PreferenceConfigurationSource implements ConfigurationSource {

	private static final String ERROR_MESSAGE_NO_INSTALLATION_DIRECTORY = 
		"The installation directory for JsUnit is not configured. " +
		"Please go to Window->Preferences->JsUnit and enter a JsUnit installation directory.";

	private static final String ERROR_MESSAGE_NO_BROWSER_PATH = 
		"No browsers are configured. " +
		"Please go to Window->Preferences->JsUnit and enter one or more browser executables.";

	private static final String ERROR_MESSAGE_INVALID_TEST_PAGE_EXTENSION =
		"Invalid Test Page extension.  Test Pages must be HTML files.";
	
	private static final String ERROR_MESSAGE_INVALID_PORT =
		"Invalid Port for JsUnit." +
		"Please go to Window->Preferences->JsUnit and enter a valid port.";

	private static final String ERROR_MESSAGE_NO_LOGS_DIRECTORY =
		"The logs directory for JsUnit is not configured." +
		"Please go to Window->Preferences->JsUnit and enter a logs directory.";
	
	private JsUnitPreferenceStore preferenceStore;
	private IFile testPage;

	public PreferenceConfigurationSource(JsUnitPreferenceStore preferenceStore, IFile testPage) {
		this.preferenceStore = preferenceStore;
		this.testPage = testPage;
	}

	public String url() {
		String installationDirectory = preferenceStore.installationDirectory();
		String protocol = "file:";
		if (installationDirectory.startsWith("/"))
			protocol += "//";
		else
			protocol += "///";
		return 
		        protocol+
		        installationDirectory +
		        java.io.File.separator +
		        "testRunner.html?" + 
		        "testPage=" +
		        testPage.getRawLocation().toString() +
		        "&autoRun=true&"+
		        "submitresults=http://localhost:" + port() + "/jsunit/acceptor";
	}

	public boolean hasError() {
		return getErrorMessage() != null;
	}

	public String getErrorMessage() {
		String installationDirectory = preferenceStore.installationDirectory();
		if (isEmpty(installationDirectory)) {
			return ERROR_MESSAGE_NO_INSTALLATION_DIRECTORY;
		}

		String browserPathsString = browserFileNames();
		String[] paths = browserPathsString.split(",");
		if (paths.length == 0 || (paths.length==1 && isEmpty(paths[0]))) {
			return ERROR_MESSAGE_NO_BROWSER_PATH;
		}
		
		if (!preferenceStore.hasValidPort()) {
			return ERROR_MESSAGE_INVALID_PORT;
		}
		
		if (isEmpty(logsDirectory())) {
			return ERROR_MESSAGE_NO_LOGS_DIRECTORY;
		}
		
		if (!isValidTestPage(testPage)) {
			return ERROR_MESSAGE_INVALID_TEST_PAGE_EXTENSION;
		}
		
		return null;
	}

	private boolean isEmpty(String string) {
		return string == null || string.trim().length() == 0;
	}
	
	private boolean isValidTestPage(IFile testPage) {
		String extension = testPage.getFileExtension();
		if (extension == null)
			return false;
		String lowerCased = extension.toLowerCase();
		
		List<String> validExtensions = preferenceStore.testPageExtensions();
		return validExtensions.contains(lowerCased);
	}

	public String resourceBase() {
		return preferenceStore.installationDirectory();
	}

	public String port() {
		return String.valueOf(preferenceStore.port());
	}

	public String logsDirectory() {
		return preferenceStore.logsDirectory();
	}

	public String browserFileNames() {
		return preferenceStore.browserFileNamesString();
	}

	public String closeBrowsersAfterTestRuns() {
		return String.valueOf(preferenceStore.closeBrowsersAfterTestRuns());
	}

	public String logStatus() {
		return String.valueOf(true);
	}
	
}