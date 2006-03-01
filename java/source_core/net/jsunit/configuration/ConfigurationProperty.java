package net.jsunit.configuration;

import net.jsunit.utility.StringUtility;
import org.jdom.Element;

import java.io.File;
import java.net.URL;
import java.util.*;

public enum ConfigurationProperty {

    BROWSER_FILE_NAMES("browserFileNames", "Browser file names", null, false, true) {
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
    CLOSE_BROWSERS_AFTER_TEST_RUNS("closeBrowsersAfterTestRuns", "Close browsers?", "true", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.shouldCloseBrowsersAfterTestRuns());
        }
    },
    LOGS_DIRECTORY("logsDirectory", "Logs directory", "." + File.separator + "logs", false, false) {
        public String getValueString(Configuration configuration) {
            return configuration.getLogsDirectory().getAbsolutePath();
        }
    },
    LOG_STATUS("logStatus", "Log status?", "true", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.shouldLogStatus());
        }
    },
    PORT("port", "Port", "8080", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.getPort());
        }
    },
    REMOTE_MACHINE_URLS("remoteMachineURLs", "Remote machine URLs", null, true, true) {
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
    RESOURCE_BASE("resourceBase", "Resource base", ".", false, false) {
        public String getValueString(Configuration configuration) {
            return configuration.getResourceBase().getAbsolutePath();
        }
    },
    TIMEOUT_SECONDS("timeoutSeconds", "Test timeout (seconds)", "60", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.getTimeoutSeconds());
        }
    },
    URL("url", "Test Page URL", null, true, false) {
        public String getValueString(Configuration configuration) {
            URL testURL = configuration.getTestURL();
            return testURL == null ? "" : testURL.toString();
        }
    },
    IGNORE_UNRESPONSIVE_REMOTE_MACHINES("ignoreUnresponsiveRemoteMachines", "Ignore unresponsive remote machines?", "false", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.shouldIgnoreUnresponsiveRemoteMachines());
        }
    },
    DESCRIPTION("description", "Description", null, false, false) {
        public String getValueString(Configuration configuration) {
            return configuration.getDescription();
        }
    };

    private String name;
    private String displayName;
    private String defaultValue;
    private boolean isURL;
    private boolean isMultiValued;

    private ConfigurationProperty(String name, String displayName, String defaultValue, boolean isURL, boolean isMultiValued) {
        this.displayName = displayName;
        this.name = name;
        this.defaultValue = defaultValue;
        this.isURL = isURL;
        this.isMultiValued = isMultiValued;
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

    public static List<ConfigurationProperty> all() {
        List<ConfigurationProperty> result = Arrays.asList(ConfigurationProperty.values());
        Collections.sort(result, comparator());
        return result;
    }

    public static Comparator<ConfigurationProperty> comparator() {
        return new Comparator<ConfigurationProperty>() {
            public int compare(ConfigurationProperty property1, ConfigurationProperty property2) {
                return property1.name().compareTo(property2.name());
            }
        };
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isURL() {
        return isURL;
    }

    public List<String> getValueStrings(Configuration configuration) {
        List<String> result = new ArrayList<String>();
        if (isMultiValued())
            for (String value : StringUtility.listFromCommaDelimitedString(getValueString(configuration)))
                result.add(value);
        else
            result.add(getValueString(configuration));
        return result;
    }

    private boolean isMultiValued() {
        return isMultiValued;
    }
}
