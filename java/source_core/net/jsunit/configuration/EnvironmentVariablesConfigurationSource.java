package net.jsunit.configuration;

public class EnvironmentVariablesConfigurationSource implements ConfigurationSource {
 
    public String resourceBase() {
        return System.getProperty(ConfigurationProperty.RESOURCE_BASE.getName());
    }

    public String port() {
        return System.getProperty(ConfigurationProperty.PORT.getName());
    }

    public String remoteMachineURLs() {
        return System.getProperty(ConfigurationProperty.REMOTE_MACHINE_URLS.getName());
    }

    public String logsDirectory() {
        return System.getProperty(ConfigurationProperty.LOGS_DIRECTORY.getName());
    }

    public String browserFileNames() {
        return System.getProperty(ConfigurationProperty.BROWSER_FILE_NAMES.getName());
    }

    public String url() {
        return System.getProperty(ConfigurationProperty.URL.getName());
    }

    public String closeBrowsersAfterTestRuns() {
		return System.getProperty(ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName());
	}

	public boolean isAppropriate() {
        return url() != null;
    }

	public String logStatus() {
		return System.getProperty(ConfigurationProperty.LOG_STATUS.getName());
	}

	public String timeoutSeconds() {
		return System.getProperty(ConfigurationProperty.TIMEOUT_SECONDS.getName());
	}

}