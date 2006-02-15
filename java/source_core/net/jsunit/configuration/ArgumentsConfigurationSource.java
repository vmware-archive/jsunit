package net.jsunit.configuration;

import java.util.Iterator;
import java.util.List;
 
/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class ArgumentsConfigurationSource implements ConfigurationSource {

    private List<String> arguments;

	public ArgumentsConfigurationSource(List<String> arguments) {
        this.arguments = arguments;
    }

    private String getArgumentValue(String argumentName) {
        for (Iterator<String> it = arguments.iterator(); it.hasNext();) {
            String argument = (String) it.next();
            if (argument.equals("-" + argumentName))
                return it.next();
        }
        return null;
    }

    public String resourceBase() {
        return getArgumentValue(ConfigurationProperty.RESOURCE_BASE.getName());
    }

    public String port() {
        return getArgumentValue(ConfigurationProperty.PORT.getName());
    }

    public String remoteMachineURLs() {
        return getArgumentValue(ConfigurationProperty.REMOTE_MACHINE_URLS.getName());
    }

    public String logsDirectory() {
        return getArgumentValue(ConfigurationProperty.LOGS_DIRECTORY.getName());
    }

    public String browserFileNames() {
        return getArgumentValue(ConfigurationProperty.BROWSER_FILE_NAMES.getName());
    }

    public String url() {
        return getArgumentValue(ConfigurationProperty.URL.getName());
    }

	public String closeBrowsersAfterTestRuns() {
		return getArgumentValue(ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName());
	}

	public String logStatus() {
		return getArgumentValue(ConfigurationProperty.LOG_STATUS.getName());
	}

	public String timeoutSeconds() {
		return getArgumentValue(ConfigurationProperty.TIMEOUT_SECONDS.getName());
	}
    
}