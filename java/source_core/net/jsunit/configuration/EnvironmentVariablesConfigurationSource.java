package net.jsunit.configuration;

public class EnvironmentVariablesConfigurationSource implements ConfigurationSource {
 
    public String resourceBase() {
        return System.getProperty(ConfigurationConstants.RESOURCE_BASE);
    }

    public String port() {
        return System.getProperty(ConfigurationConstants.PORT);
    }

    public String logsDirectory() {
        return System.getProperty(ConfigurationConstants.LOGS_DIRECTORY);
    }

    public String browserFileNames() {
        return System.getProperty(ConfigurationConstants.BROWSER_FILE_NAMES);
    }

    public String url() {
        return System.getProperty(ConfigurationConstants.URL);
    }

    public String closeBrowsersAfterTestRuns() {
		return System.getProperty(ConfigurationConstants.CLOSE_BROWSERS_AFTER_TEST_RUNS);
	}

	public boolean isAppropriate() {
        return url() != null;
    }

	public String logStatus() {
		return System.getProperty(ConfigurationConstants.LOG_STATUS);
	}

	public String timeoutSeconds() {
		return System.getProperty(ConfigurationConstants.TIMEOUT_SECONDS);
	}

}