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
        return getArgumentValue(ConfigurationConstants.RESOURCE_BASE);
    }

    public String port() {
        return getArgumentValue(ConfigurationConstants.PORT);
    }

    public String logsDirectory() {
        return getArgumentValue(ConfigurationConstants.LOGS_DIRECTORY);
    }

    public String browserFileNames() {
        return getArgumentValue(ConfigurationConstants.BROWSER_FILE_NAMES);
    }

    public String url() {
        return getArgumentValue(ConfigurationConstants.URL);
    }

	public String closeBrowsersAfterTestRuns() {
		return getArgumentValue(ConfigurationConstants.CLOSE_BROWSERS_AFTER_TEST_RUNS);
	}

	public String logStatus() {
		return getArgumentValue(ConfigurationConstants.LOG_STATUS);
	}    
    
}