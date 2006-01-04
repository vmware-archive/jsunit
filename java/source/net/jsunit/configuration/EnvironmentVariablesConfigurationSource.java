package net.jsunit.configuration;

public class EnvironmentVariablesConfigurationSource implements ConfigurationSource {
 
    public String resourceBase() {
        return System.getProperty(RESOURCE_BASE);
    }

    public String port() {
        return System.getProperty(PORT);
    }

    public String logsDirectory() {
        return System.getProperty(LOGS_DIRECTORY);
    }

    public String browserFileNames() {
        return System.getProperty(BROWSER_FILE_NAMES);
    }

    public String url() {
        return System.getProperty(URL);
    }

    public String closeBrowsersAfterTestRuns() {
		return System.getProperty(CLOSE_BROWSERS_AFTER_TEST_RUNS);
	}

	public boolean isAppropriate() {
        return url() != null;
    }

}