package net.jsunit.plugin.intellij.configuration;

import junit.framework.TestCase;
import net.jsunit.configuration.ConfigurationSource;

public class ConfigurationComponentTest extends TestCase {

    public void testSimple() throws Exception {
        ConfigurationComponent component = new ConfigurationComponent();
        component.setBrowserFileNames(new String[]{"browser1.exe", "browser2.exe"});
        component.setCloseBrowserAfterTestRuns(true);
        component.setLogsDirectory("logs directory");
        component.setLogStatusToConsole(true);
        component.setTestPageExtensions("html, htm");
        component.setTimeoutSeconds(250);
        ConfigurationSource source = component.asConfigurationSource();
        assertEquals("browser1.exe,browser2.exe", source.browserFileNames());
        assertEquals("true", source.closeBrowsersAfterTestRuns());
        assertEquals("logs directory", source.logsDirectory());
        assertEquals("true", source.logStatus());
        assertEquals("250", source.timeoutSeconds());
    }

}
