package net.jsunit.configuration;

import net.jsunit.utility.StringUtility;
import org.jdom.Element;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;
import java.io.File;

public enum ConfigurationProperty {

    BROWSER_FILE_NAMES("browserFileNames", "Browser file names", false, true) {

        public String getValueString(Configuration configuration) {
            return StringUtility.commaSeparatedString(configuration.getBrowserFileNames());
        }

        protected void addContentTo(Configuration configuration, Element element) {
            List<String> browserFileNames = configuration.getBrowserFileNames();
            for (int i = 0; i< browserFileNames.size(); i++) {
                String fileName = browserFileNames.get(i);
                Element fileNameElement = new Element("browserFileName");
                fileNameElement.setAttribute("id", String.valueOf(i));
                fileNameElement.setText(fileName);
                element.addContent(fileNameElement);
            }
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String browserFileNamesString = source.browserFileNames();
            try {
                configuration.setBrowserFileNames(StringUtility.listFromCommaDelimitedString(browserFileNamesString));
            } catch (Exception e) {
                throw new ConfigurationException(this, browserFileNamesString, e);
            }
        }
    },

    CLOSE_BROWSERS_AFTER_TEST_RUNS("closeBrowsersAfterTestRuns", "Close browsers?", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.shouldCloseBrowsersAfterTestRuns());
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String string = source.closeBrowsersAfterTestRuns();
            if (StringUtility.isEmpty(string))
                string = String.valueOf(true);
            configuration.setCloseBrowsersAfterTestRuns(Boolean.valueOf(string));
        }
    },

    DESCRIPTION("description", "Description", false, false) {
        public String getValueString(Configuration configuration) {
            String description = configuration.getDescription();
            if (StringUtility.isEmpty(description))
                return "";
            return description;
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            configuration.setDescription(source.description());
        }
    },

    IGNORE_UNRESPONSIVE_REMOTE_MACHINES("ignoreUnresponsiveRemoteMachines", "Ignore unresponsive remote machines?", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.shouldIgnoreUnresponsiveRemoteMachines());
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String string = source.ignoreUnresponsiveRemoteMachines();
            if (StringUtility.isEmpty(string))
                string = String.valueOf(false);
            configuration.setIgnoreUnresponsiveRemoteMachines(Boolean.valueOf(string));
        }
    },

    LOGS_DIRECTORY("logsDirectory", "Logs directory", false, false) {
        public String getValueString(Configuration configuration) {
            return configuration.getLogsDirectory().getAbsolutePath();
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String logsDirectoryString = source.logsDirectory();
            try {
                if (StringUtility.isEmpty(logsDirectoryString))
                    logsDirectoryString = "logs";
                configuration.setLogsDirectory(new File(logsDirectoryString));
            } catch (Exception e) {
                throw new ConfigurationException(this, logsDirectoryString, e);
            }

        }
    },

    LOG_STATUS("logStatus", "Log status?", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.shouldLogStatus());
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String logStatus = source.logStatus();
            if (StringUtility.isEmpty(logStatus))
                logStatus = String.valueOf(true);
            configuration.setShouldLogStatus(Boolean.valueOf(logStatus));
        }
    },

    PORT("port", "Port", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.getPort());
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String portString = source.port();
            if (StringUtility.isEmpty(portString))
                portString = "8080";
            try {
                configuration.setPort(Integer.parseInt(portString));
            } catch (NumberFormatException e) {
                throw new ConfigurationException(this, portString, e);
            }
        }
    },

    REMOTE_MACHINE_URLS("remoteMachineURLs", "Remote machine URLs", true, true) {
        public String getValueString(Configuration configuration) {
            return StringUtility.commaSeparatedString(configuration.getRemoteMachineURLs());
        }

        protected void addContentTo(Configuration configuration, Element element) {
            for (int i = 0; i < configuration.getRemoteMachineURLs().size(); i++) {
                URL remoteMachineURL = configuration.getRemoteMachineURLs().get(i);
                Element urlElement = new Element("remoteMachineURL");
                urlElement.setAttribute("id", String.valueOf(i));
                urlElement.setText(remoteMachineURL.toString());
                element.addContent(urlElement);
            }
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String remoteMachineURLs = source.remoteMachineURLs();
            List<String> strings = StringUtility.listFromCommaDelimitedString(remoteMachineURLs);
            List<URL> result = new ArrayList<URL>(strings.size());
            for (String string : strings)
                try {
                    URL attemptedURL = new URL(string);
                    result.add(new URL(attemptedURL.getProtocol(), attemptedURL.getHost(), attemptedURL.getPort(), "/jsunit"));
                } catch (MalformedURLException e) {
                    throw new ConfigurationException(this, remoteMachineURLs, e);
                }
            configuration.setRemoteMachineURLs(result);
        }
    },

    RESOURCE_BASE("resourceBase", "Resource base", false, false) {
        public String getValueString(Configuration configuration) {
            return configuration.getResourceBase().getAbsolutePath();
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String resourceBaseString = source.resourceBase();
            if (StringUtility.isEmpty(resourceBaseString))
                resourceBaseString = ".";
            try {
                configuration.setResourceBase(new File(resourceBaseString));
            } catch (Exception e) {
                throw new ConfigurationException(this, resourceBaseString, e);
            }
        }
    },

    TIMEOUT_SECONDS("timeoutSeconds", "Test timeout (seconds)", false, false) {
        public String getValueString(Configuration configuration) {
            return String.valueOf(configuration.getTimeoutSeconds());
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String timeoutSecondsString = source.timeoutSeconds();
            if (StringUtility.isEmpty(timeoutSecondsString))
                timeoutSecondsString = "60";
            try {
                configuration.setTimeoutSeconds(Integer.parseInt(timeoutSecondsString));
            } catch (NumberFormatException e) {
                throw new ConfigurationException(ConfigurationProperty.TIMEOUT_SECONDS, timeoutSecondsString, e);
            }

        }
    },

    URL("url", "Test Page URL", true, false) {
        public String getValueString(Configuration configuration) {
            URL testURL = configuration.getTestURL();
            return testURL == null ? "" : testURL.toString();
        }

        public void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException {
            String urlString = source.url();
            if (StringUtility.isEmpty(urlString))
                return;
            try {
                configuration.setTestURL(new URL(urlString));
            } catch (MalformedURLException e) {
                throw new ConfigurationException(this, urlString, e);
            }

        }
    };

    private String name;
    private String displayName;
    private boolean isURL;
    private boolean isMultiValued;

    private ConfigurationProperty(String name, String displayName, boolean isURL, boolean isMultiValued) {
        this.displayName = displayName;
        this.name = name;
        this.isURL = isURL;
        this.isMultiValued = isMultiValued;
    }

    public String getName() {
        return name;
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

    public abstract void configure(Configuration configuration, ConfigurationSource source) throws ConfigurationException;

}
