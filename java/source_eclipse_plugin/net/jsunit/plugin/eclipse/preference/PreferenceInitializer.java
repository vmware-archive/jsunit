package net.jsunit.plugin.eclipse.preference;

import net.jsunit.configuration.ConfigurationProperty;
import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = JsUnitPlugin.soleInstance().getPreferenceStore();
		store.setDefault(PreferenceConstants.PREFERENCE_BROWSER_FILE_NAMES, "");
		store.setDefault(PreferenceConstants.PREFERENCE_CLOSE_BROWSERS_AFTER_TEST_RUNS, Boolean.valueOf(ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getDefaultValue()));
		store.setDefault(PreferenceConstants.PREFERENCE_INSTALLATION_DIRECTORY, "");
		store.setDefault(PreferenceConstants.PREFERENCE_LOG_STATUS, false);
		store.setDefault(PreferenceConstants.PREFERENCE_TEST_PAGE_EXTENSIONS, "html,htm");
		store.setDefault(PreferenceConstants.PREFERENCE_TIMEOUT_SECONDS, ConfigurationProperty.TIMEOUT_SECONDS.getDefaultValue());
		store.setDefault(PreferenceConstants.PREFERENCE_USE_DEFAULT_LOGS_DIRECTORY, true);
	}

}
