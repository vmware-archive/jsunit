package net.jsunit.configuration;

import java.util.Iterator;
import java.util.List;
 
/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class ArgumentsConfigurationSource implements ConfigurationSource {
    private String port;
    private String resourceBase;
    private String logsDirectory;
    private String url;
    private String browserFileNames;
    private String closeBrowsersAfterTestRuns;

    public ArgumentsConfigurationSource(List args) {
        init(args);
    }

    private void init(List args) {
        for (Iterator it = args.iterator(); it.hasNext();) {
            String argument = (String) it.next();
            if (argument.equals("-" + ConfigurationConstants.PORT))
                this.port = (String) it.next();
            else if (argument.equals("-" + ConfigurationConstants.RESOURCE_BASE))
                this.resourceBase = (String) it.next();
            else if (argument.equals("-" + ConfigurationConstants.LOGS_DIRECTORY))
                this.logsDirectory = (String) it.next();
            else if (argument.equals("-" + ConfigurationConstants.URL))
                this.url = (String) it.next();
            else if (argument.equals("-" + ConfigurationConstants.BROWSER_FILE_NAMES))
                this.browserFileNames = (String) it.next();
            else if (argument.equals("-" + ConfigurationConstants.CLOSE_BROWSERS_AFTER_TEST_RUNS))
                this.closeBrowsersAfterTestRuns= (String) it.next();
        }
    }

    public String resourceBase() {
        return resourceBase;
    }

    public String port() {
        return port;
    }

    public String logsDirectory() {
        return logsDirectory;
    }

    public String browserFileNames() {
        return browserFileNames;
    }

    public String url() {
        return url;
    }

	public String closeBrowsersAfterTestRuns() {
		return closeBrowsersAfterTestRuns;
	}
    
    
}