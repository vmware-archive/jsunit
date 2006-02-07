package net.jsunit.plugin.eclipse.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import net.jsunit.configuration.Configuration;
import net.jsunit.plugin.eclipse.JsUnitPlugin;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = JsUnitPlugin.soleInstance().getPreferenceStore();
		store.setDefault(PreferenceConstants.PREFERENCE_INSTALLATION_DIRECTORY, "");
		store.setDefault(PreferenceConstants.PREFERENCE_USE_DEFAULT_LOGS_DIRECTORY, true);
		store.setDefault(PreferenceConstants.PREFERENCE_BROWSER_FILE_NAMES, "");
		store.setDefault(PreferenceConstants.PREFERENCE_CLOSE_BROWSERS_AFTER_TEST_RUNS, true);
		store.setDefault(PreferenceConstants.PREFERENCE_TEST_PAGE_EXTENSIONS, "html,htm");
		store.setDefault(PreferenceConstants.PREFERENCE_TIMEOUT_SECONDS, String.valueOf(Configuration.DEFAULT_TIMEOUT_SECONDS));
		store.setDefault(PreferenceConstants.PREFERENCE_LOG_STATUS, false);
	}

}
