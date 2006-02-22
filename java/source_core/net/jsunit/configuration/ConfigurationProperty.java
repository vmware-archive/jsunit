package net.jsunit.configuration;

import java.io.File;
import java.net.URL;

import net.jsunit.utility.StringUtility;

import org.jdom.Element;

public enum ConfigurationProperty {

    BROWSER_FILE_NAMES("browserFileNames", null) {
    	public String getValueString(Configuration configuration) {
    		return StringUtility.commaSeparatedString(configuration.getBrowserFileNames());
    	}
    	protected void addContentTo(Configuration configuration, Element element) {
			for (String name : configuration.getBrowserFileNames()) {
				Element browserFileName = new Element("browserFileName");
				browserFileName.setText(name);
				element.addContent(browserFileName);
			}
    	}
    },
    CLOSE_BROWSERS_AFTER_TEST_RUNS("closeBrowsersAfterTestRuns", "true") {
    	public String getValueString(Configuration configuration) {
    		return String.valueOf(configuration.shouldCloseBrowsersAfterTestRuns());
    	}
    },
    LOGS_DIRECTORY("logsDirectory", "." + File.separator + "logs") {
    	public String getValueString(Configuration configuration) {
    		return configuration.getLogsDirectory().toString();
    	}
    },
    LOG_STATUS("logStatus", "true") {
    	public String getValueString(Configuration configuration) {
    		return String.valueOf(configuration.shouldLogStatus());
    	}
    },
    PORT("port", "8080") {
    	public String getValueString(Configuration configuration) {
    		return String.valueOf(configuration.getPort());
    	}
    },
    REMOTE_MACHINE_URLS("remoteMachineURLs", null) {
    	public String getValueString(Configuration configuration) {
    		return StringUtility.commaSeparatedString(configuration.getRemoteMachineURLs());
    	}
    	protected void addContentTo(Configuration configuration, Element element) {
    		for (URL remoteMachineURL : configuration.getRemoteMachineURLs()) {
                Element urlElement = new Element("remoteMachineURL");
                urlElement.setText(remoteMachineURL.toString());
                element.addContent(urlElement);
            }
    	}
    },
    RESOURCE_BASE("resourceBase", ".") {
    	public String getValueString(Configuration configuration) {
    		return configuration.getResourceBase().toString();
    	}
    },
    TIMEOUT_SECONDS("timeoutSeconds", "60") {
    	public String getValueString(Configuration configuration) {
    		return String.valueOf(configuration.getTimeoutSeconds());
    	}
    },
    URL("url", null) {
    	public String getValueString(Configuration configuration) {
    		URL testURL = configuration.getTestURL();
			return testURL == null ? "" : testURL.toString();
    	}
    };

    private String name;
    private String defaultValue;

    ConfigurationProperty(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

	public abstract String getValueString(Configuration configuration);

	public void addXmlTo(Element parentElement, Configuration configuration) {
        Element element = new Element(name);
        addContentTo(configuration, element);
        parentElement.addContent(element);
	}

	protected void addContentTo(Configuration configuration, Element element) {
		element.setText(getValueString(configuration));
	}

}
