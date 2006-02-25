package net.jsunit.configuration;

import net.jsunit.utility.StringUtility;
import org.jdom.Element;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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
    },
    IGNORE_UNRESPONSIVE_REMOTE_MACHINES("ignoreUnresponsiveRemoteMachines", "false") {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.shouldIgnoreUnresponsiveRemoteMachines());
        }
    },
    DESCRIPTION("description", null) {
        public String getValueString(Configuration configuration) {
            return configuration.getDescription();
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
}
