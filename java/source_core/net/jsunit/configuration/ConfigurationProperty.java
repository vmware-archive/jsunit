package net.jsunit.configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.jsunit.Utility;

public enum ConfigurationProperty {

    PORT("port", "8080") {
    	public String getValueString(Configuration configuration) {
    		return String.valueOf(configuration.getPort());
    	}
    },
    RESOURCE_BASE("resourceBase", ".") {
    	public String getValueString(Configuration configuration) {
    		return configuration.getResourceBase().toString();
    	}
    },
    LOGS_DIRECTORY("logsDirectory", "." + File.separator + "logs") {
    	public String getValueString(Configuration configuration) {
    		return configuration.getLogsDirectory().toString();
    	}
    },
    URL("url", null) {
    	public String  getValueString(Configuration configuration) {
    		return configuration.getTestURL().toString();
    	}
    },
    BROWSER_FILE_NAMES("browserFileNames", null) {
    	public String getValueString(Configuration configuration) {
    		return Utility.commaSeparatedString(configuration.getBrowserFileNames());
    	}
    },
    CLOSE_BROWSERS_AFTER_TEST_RUNS("closeBrowsersAfterTestRuns", "true") {
    	public String getValueString(Configuration configuration) {
    		return String.valueOf(configuration.shouldCloseBrowsersAfterTestRuns());
    	}
    },
    LOG_STATUS("logStatus", "true") {
    	public String getValueString(Configuration configuration) {
    		return String.valueOf(configuration.shouldLogStatus());
    	}
    },
    TIMEOUT_SECONDS("timeoutSeconds", "60") {
    	public String getValueString(Configuration configuration) {
    		return String.valueOf(configuration.getTimeoutSeconds());
    	}
    },
    REMOTE_MACHINE_URLS("remoteMachineURLs", null) {
    	public String getValueString(Configuration configuration) {
    		return Utility.commaSeparatedString(configuration.getRemoteMachineURLs());
    	}
    };

    private String name;
    private String defaultValue;
	private final List<ConfigurationType> serverTypes;

    ConfigurationProperty(String name, String defaultValue, ConfigurationType... serverTypes) {
        this.name = name;
        this.defaultValue = defaultValue;
		this.serverTypes = Arrays.asList(serverTypes);
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequiredFor(ConfigurationType type) {
        return serverTypes.contains(type);
    }

	public abstract String getValueString(Configuration configuration);

}
