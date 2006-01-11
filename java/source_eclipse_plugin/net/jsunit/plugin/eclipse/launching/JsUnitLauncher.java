package net.jsunit.plugin.eclipse.launching;

import net.jsunit.BrowserTestRunListener;
import net.jsunit.JsUnitServer;
import net.jsunit.TestRunManager;
import net.jsunit.configuration.Configuration;
import net.jsunit.plugin.eclipse.DefaultErrorMessageRenderer;
import net.jsunit.plugin.eclipse.ErrorMessageRenderer;
import net.jsunit.plugin.eclipse.JsUnitPlugin;
import net.jsunit.plugin.eclipse.preference.JsUnitPreferenceStore;
import net.jsunit.plugin.eclipse.preference.PreferenceConfigurationSource;

import org.eclipse.core.resources.IFile;

public class JsUnitLauncher {
	
	private JsUnitPreferenceStore preferenceStore;
	private ErrorMessageRenderer errorMessageRenderer;
	private BrowserTestRunListener browserTestRunListener;
	private BrowserTestRunnerFactory serverFactory;

	public JsUnitLauncher() {
		this(JsUnitPlugin.soleInstance().getJsUnitPreferenceStore(), new DefaultErrorMessageRenderer(), JsUnitPlugin.soleInstance().getTestRunViewPart(), new DefaultJsUnitServerFactory());
	}
	
	public JsUnitLauncher(JsUnitPreferenceStore preferenceStore, ErrorMessageRenderer errorMessageRenderer, BrowserTestRunListener browserTestRunListener, BrowserTestRunnerFactory factory) {
		this.preferenceStore = preferenceStore;
		this.errorMessageRenderer = errorMessageRenderer;
		this.browserTestRunListener = browserTestRunListener;
		this.serverFactory = factory;
	}
	
	public void launch(IFile testPage) {
		PreferenceConfigurationSource source = preferenceStore.asConfigurationSource(testPage);
		if (source.hasError()) {
			showConfigurationErrorMessage(source.getErrorMessage());
			return;
		}
		disposeOfExistingJsUnitServer();
		JsUnitServer server = createAndStartServer(source);
		TestRunManager manager = new TestRunManager(server);
		try {
			manager.runTests();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stopServer(server);
		}
	}

	private void disposeOfExistingJsUnitServer() {
		JsUnitServer instance = JsUnitServer.instance();
		if (instance != null)
			instance.dispose();
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
		JsUnitServer server = serverFactory.create(configuration);
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