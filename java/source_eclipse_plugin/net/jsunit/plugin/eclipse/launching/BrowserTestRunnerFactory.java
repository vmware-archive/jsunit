package net.jsunit.plugin.eclipse.launching;

import net.jsunit.JsUnitServer;
import net.jsunit.configuration.Configuration;

public interface BrowserTestRunnerFactory {

	JsUnitServer create(Configuration configuration);

}
