package net.jsunit.plugin.eclipse.preference;

import java.util.List;

import net.jsunit.utility.StringUtility;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.preference.IPreferenceStore;

public class JsUnitPreferenceStore {

	private IPreferenceStore preferenceStore;

	public JsUnitPreferenceStore(IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
	}
	
	public String browserFileNamesString() {
		return preferenceStore.getString(PreferenceConstants.PREFERENCE_BROWSER_FILE_NAMES);
	}
	
	public String installationDirectory() {
		return preferenceStore.getString(PreferenceConstants.PREFERENCE_INSTALLATION_DIRECTORY);
	}
	
	public String logsDirectory() {
		return preferenceStore.getString(PreferenceConstants.PREFERENCE_LOGS_DIRECTORY);
	}

	public boolean closeBrowsersAfterTestRuns() {
		return preferenceStore.getBoolean(PreferenceConstants.PREFERENCE_CLOSE_BROWSERS_AFTER_TEST_RUNS);
	}
	
	public String testPageExtensionsString() {
		return preferenceStore.getString(PreferenceConstants.PREFERENCE_TEST_PAGE_EXTENSIONS);
	}
	
	public List<String> testPageExtensions() {
		return StringUtility.listFromCommaDelimitedString(testPageExtensionsString());
	}

	public int timeoutSeconds() {
		return preferenceStore.getInt(PreferenceConstants.PREFERENCE_TIMEOUT_SECONDS);
	}

	public List<String> browserFileNames() {
		return StringUtility.listFromCommaDelimitedString(browserFileNamesString());
	}

	public PreferenceConfigurationSource asConfigurationSource(IFile testPage, int jsUnitServerPort) {
		return new PreferenceConfigurationSource(this, testPage, jsUnitServerPort);
	}

	public boolean logStatus() {
		return preferenceStore.getBoolean(PreferenceConstants.PREFERENCE_LOG_STATUS);
	}

}