package net.jsunit.plugin.eclipse.preference;

import java.util.ArrayList;
import java.util.List;


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
	
	public int port() {
		return preferenceStore.getInt(PreferenceConstants.PREFERENCE_PORT);
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
		return listFromDelimitedString(testPageExtensionsString());
	}

	private List<String> listFromDelimitedString(String delimitedString) {
		String[] array = delimitedString.split(",");
		List<String> result = new ArrayList<String>(array.length);
		for (String element : array)
			result.add(element.trim());
		return result;
	}
	
	public List<String> browserFileNames() {
		return listFromDelimitedString(browserFileNamesString());
	}

	public boolean hasValidPort() {
		return port() > 0;
	}

	public PreferenceConfigurationSource asConfigurationSource(IFile testPage) {
		return new PreferenceConfigurationSource(this, testPage);
	}

}