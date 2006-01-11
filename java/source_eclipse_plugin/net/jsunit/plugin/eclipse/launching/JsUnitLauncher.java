package net.jsunit.plugin.eclipse.launching;

import net.jsunit.BrowserTestRunListener;
import net.jsunit.JsUnitServer;
import net.jsunit.StandaloneTest;
import net.jsunit.configuration.Configuration;
import net.jsunit.plugin.eclipse.DefaultErrorMessageRenderer;
import net.jsunit.plugin.eclipse.DefaultStandaloneTestRunner;
import net.jsunit.plugin.eclipse.ErrorMessageRenderer;
import net.jsunit.plugin.eclipse.JsUnitPlugin;
import net.jsunit.plugin.eclipse.StandaloneTestRunner;
import net.jsunit.plugin.eclipse.preference.JsUnitPreferenceStore;
import net.jsunit.plugin.eclipse.preference.PreferenceConfigurationSource;

import org.eclipse.core.resources.IFile;

public class JsUnitLauncher {
	
	private JsUnitPreferenceStore preferenceStore;
	private StandaloneTestRunner testRunner;
	private ErrorMessageRenderer errorMessageRenderer;
	private BrowserTestRunListener browserTestRunListener;

	public JsUnitLauncher() {
		this(JsUnitPlugin.soleInstance().getJsUnitPreferenceStore(), new DefaultStandaloneTestRunner(), new DefaultErrorMessageRenderer(), JsUnitPlugin.soleInstance().getTestRunViewPart());
	}
	
	public JsUnitLauncher(JsUnitPreferenceStore preferenceStore, StandaloneTestRunner testRunner, ErrorMessageRenderer errorMessageRenderer, BrowserTestRunListener browserTestRunListener) {
		this.preferenceStore = preferenceStore;
		this.testRunner = testRunner;
		this.errorMessageRenderer = errorMessageRenderer;
		this.browserTestRunListener = browserTestRunListener;
	}
	
	public void launch(IFile testPage) {
		PreferenceConfigurationSource source = preferenceStore.asConfigurationSource(testPage);
		if (source.hasError()) {
			showConfigurationErrorMessage(source.getErrorMessage());
			return;
		}
		JsUnitServer server = createAndStartServer(source);
		StandaloneTest test = new StandaloneTest("testStandaloneRun");
		test.setBrowserTestRunner(server);
		testRunner.runStandaloneTest(test);
		stopServer(server);
	}

	private void stopServer(JsUnitServer server) {
		try {
			server.dispose();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private JsUnitServer createAndStartServer(PreferenceConfigurationSource source) {
		Configuration configuration = new Configuration(source);
		configuration.setNeedsLogging(false);
		JsUnitServer server = new JsUnitServer(configuration);
		server.addBrowserTestRunListener(browserTestRunListener);
		try {
			server.start();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
		return server;
	}

	private void showConfigurationErrorMessage(String errorMessage) {
		errorMessageRenderer.showError(
			"JsUnit",
			errorMessage
		);
	}

}