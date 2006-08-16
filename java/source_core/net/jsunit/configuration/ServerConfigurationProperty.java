package net.jsunit.configuration;

import net.jsunit.model.Browser;
import net.jsunit.utility.StringUtility;
import org.jdom.Element;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public enum ServerConfigurationProperty implements ConfigurationProperty {

    BROWSER_FILE_NAMES("browserFileNames", "Browser file names", false, true) {

        public String getValueString(ServerConfiguration configuration) {
            StringBuffer buffer = new StringBuffer();
            for (Iterator it = configuration.getBrowsers().iterator(); it.hasNext();) {
                Browser browser = (Browser) it.next();
                buffer.append(browser.getFullFileName());
                if (it.hasNext())
                    buffer.append(",");
            }
            return buffer.toString();
        }

        protected void addContentTo(ServerConfiguration configuration, Element element) {
            for (Browser browser : configuration.getBrowsers()) {
                Element fileNameElement = new Element("browserFileName");
                fileNameElement.setAttribute("id", String.valueOf(browser.getId()));
                fileNameElement.setText(browser.getFullFileName());
                element.addContent(fileNameElement);
            }
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
            String browserFileNamesString = source.browserFileNames();
            try {
                List<String> browserFileNames = StringUtility.listFromCommaDelimitedString(browserFileNamesString);
                Collections.sort(browserFileNames);
                Set<String> alreadyAddedBrowserFileNames = new HashSet<String>();
                int id = 0;
                List<Browser> browsers = new ArrayList<Browser>();
                for (String browserFileName : browserFileNames) {
                    if (!alreadyAddedBrowserFileNames.contains(browserFileName) || Browser.DEFAULT_SYSTEM_BROWSER.equals(browserFileName))
                    {
                        browsers.add(new Browser(browserFileName, id++));
                        alreadyAddedBrowserFileNames.add(browserFileName);
                    }
                }
                configuration.setBrowsers(browsers);
            } catch (Exception e) {
                throw new ConfigurationException(this, browserFileNamesString, e);
            }
        }
    },

    CLOSE_BROWSERS_AFTER_TEST_RUNS("closeBrowsersAfterTestRuns", "Close browsers?", false, false) {
        public String getValueString(ServerConfiguration configuration) {
            return String.valueOf(configuration.shouldCloseBrowsersAfterTestRuns());
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
            String string = source.closeBrowsersAfterTestRuns();
            if (StringUtility.isEmpty(string))
                string = String.valueOf(true);
            configuration.setCloseBrowsersAfterTestRuns(Boolean.valueOf(string));
        }
    },

    DESCRIPTION("description", "Description", false, false) {
        public String getValueString(ServerConfiguration configuration) {
            String description = configuration.getDescription();
            if (StringUtility.isEmpty(description))
                return "";
            return description;
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
            configuration.setDescription(source.description());
        }
    },

    IGNORE_UNRESPONSIVE_REMOTE_MACHINES("ignoreUnresponsiveRemoteMachines", "Ignore unresponsive remote machines?", false, false) {
        public String getValueString(ServerConfiguration configuration) {
            return String.valueOf(configuration.shouldIgnoreUnresponsiveRemoteMachines());
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
            String string = source.ignoreUnresponsiveRemoteMachines();
            if (StringUtility.isEmpty(string))
                string = String.valueOf(false);
            configuration.setIgnoreUnresponsiveRemoteMachines(Boolean.valueOf(string));
        }
    },

    LOGS_DIRECTORY("logsDirectory", "Logs directory", false, false) {
        public String getValueString(ServerConfiguration configuration) {
            return configuration.getLogsDirectory().getAbsolutePath();
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
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

    PORT("port", "Port", false, false) {
        public String getValueString(ServerConfiguration configuration) {
            return String.valueOf(configuration.getPort());
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
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
        public String getValueString(ServerConfiguration configuration) {
            return StringUtility.commaSeparatedString(configuration.getRemoteMachineURLs());
        }

        protected void addContentTo(ServerConfiguration configuration, Element element) {
            for (int i = 0; i < configuration.getRemoteMachineURLs().size(); i++) {
                URL remoteMachineURL = configuration.getRemoteMachineURLs().get(i);
                Element urlElement = new Element("remoteMachineURL");
                urlElement.setAttribute("id", String.valueOf(i));
                urlElement.setText(remoteMachineURL.toString());
                element.addContent(urlElement);
            }
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
            String remoteMachineURLs = source.remoteMachineURLs();
            List<String> strings = StringUtility.listFromCommaDelimitedString(remoteMachineURLs);
            Collections.sort(strings);
            List<URL> result = new ArrayList<URL>(strings.size());
            Set<String> alreadyAddedURLStrings = new HashSet<String>();
            for (String string : strings)
                try {
                    URL attemptedURL = new URL(string);
                    URL normalizedURL = new URL(attemptedURL.getProtocol(), attemptedURL.getHost(), attemptedURL.getPort(), "/jsunit");
                    if (!alreadyAddedURLStrings.contains(normalizedURL.toString())) {
                        result.add(normalizedURL);
                        alreadyAddedURLStrings.add(normalizedURL.toString());
                    }
                } catch (MalformedURLException e) {
                    throw new ConfigurationException(this, remoteMachineURLs, e);
                }
            configuration.setRemoteMachineURLs(result);
        }
    },

    RESOURCE_BASE("resourceBase", "Resource base", false, false) {
        public String getValueString(ServerConfiguration configuration) {
            return configuration.getResourceBase().getAbsolutePath();
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
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
        public String getValueString(ServerConfiguration configuration) {
            return String.valueOf(configuration.getTimeoutSeconds());
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
            String timeoutSecondsString = source.timeoutSeconds();
            if (StringUtility.isEmpty(timeoutSecondsString))
                timeoutSecondsString = "60";
            try {
                configuration.setTimeoutSeconds(Integer.parseInt(timeoutSecondsString));
            } catch (NumberFormatException e) {
                throw new ConfigurationException(ServerConfigurationProperty.TIMEOUT_SECONDS, timeoutSecondsString, e);
            }

        }
    },

    URL("url", "Test Page URL", true, false) {
        public String getValueString(ServerConfiguration configuration) {
            URL testURL = configuration.getTestURL();
            return testURL == null ? "" : testURL.toString();
        }

        public void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException {
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

    private ServerConfigurationProperty(String name, String displayName, boolean isURL, boolean isMultiValued) {
        this.displayName = displayName;
        this.name = name;
        this.isURL = isURL;
        this.isMultiValued = isMultiValued;
    }

    public String getName() {
        return name;
    }

    public abstract String getValueString(ServerConfiguration configuration);

    public void addXmlTo(Element parentElement, ServerConfiguration configuration) {
        Element element = new Element(name);
        addContentTo(configuration, element);
        parentElement.addContent(element);
    }

    protected void addContentTo(ServerConfiguration configuration, Element element) {
        element.setText(getValueString(configuration));
    }

    public static List<ServerConfigurationProperty> all() {
        List<ServerConfigurationProperty> result = Arrays.asList(ServerConfigurationProperty.values());
        Collections.sort(result, comparator());
        return result;
    }

    public static Comparator<ServerConfigurationProperty> comparator() {
        return new Comparator<ServerConfigurationProperty>() {
            public int compare(ServerConfigurationProperty property1, ServerConfigurationProperty property2) {
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

    public List<String> getValueStrings(ServerConfiguration configuration) {
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

    public abstract void configure(ServerConfiguration configuration, ConfigurationSource source) throws ConfigurationException;

    public String toString(ServerConfiguration configuration) {
        return name + " = " + getValueString(configuration);
    }

}
