package net.jsunit.plugin.eclipse.launching;

import net.jsunit.JsUnitServer;
import net.jsunit.configuration.Configuration;

public class DefaultJsUnitServerFactory implements BrowserTestRunnerFactory {

	public JsUnitServer create(Configuration configuration) {
		return new JsUnitServer(configuration);
	}

}
