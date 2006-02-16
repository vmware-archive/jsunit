package net.jsunit.configuration;

import java.util.List;
 
/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class ArgumentsConfigurationSource implements ConfigurationSource {

    private List<String> arguments;

	public ArgumentsConfigurationSource(List<String> arguments) {
        this.arguments = arguments;
    }

    private String getArgumentValue(ConfigurationProperty property) {
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i).equals("-" + property.getName())) {
            	String value = arguments.get(i+1);
                if (!value.startsWith("-"))
                	return value;
                else
                	return "";
            }
        }
        return null;
    }

    public String resourceBase() {
        return getArgumentValue(ConfigurationProperty.RESOURCE_BASE);
    }

    public String port() {
        return getArgumentValue(ConfigurationProperty.PORT);
    }

    public String remoteMachineURLs() {
        return getArgumentValue(ConfigurationProperty.REMOTE_MACHINE_URLS);
    }

    public String logsDirectory() {
        return getArgumentValue(ConfigurationProperty.LOGS_DIRECTORY);
    }

    public String browserFileNames() {
        return getArgumentValue(ConfigurationProperty.BROWSER_FILE_NAMES);
    }

    public String url() {
        return getArgumentValue(ConfigurationProperty.URL);
    }

	public String closeBrowsersAfterTestRuns() {
		return getArgumentValue(ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS);
	}

	public String logStatus() {
		return getArgumentValue(ConfigurationProperty.LOG_STATUS);
	}

	public String timeoutSeconds() {
		return getArgumentValue(ConfigurationProperty.TIMEOUT_SECONDS);
	}
    
}