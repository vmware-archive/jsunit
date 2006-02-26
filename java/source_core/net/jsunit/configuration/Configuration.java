package net.jsunit.configuration;

import net.jsunit.utility.SystemUtility;
import net.jsunit.utility.StringUtility;
import org.jdom.Element;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Configuration {

    private ConfigurationSource source;

    public Configuration(ConfigurationSource source) {
        this.source = source;
    }

    public static Configuration resolve(String[] arguments) {
        ConfigurationSource source;
        if (arguments.length > 0)
            source = new ArgumentsConfigurationSource(Arrays.asList(arguments));
        else
            source = resolveSource();
        return new Configuration(source);
    }

    public static ConfigurationSource resolveSource() {
        for (ConfigurationProperty property : ConfigurationProperty.values())
            if (System.getProperty(property.getName()) != null)
                return new EnvironmentVariablesConfigurationSource();
        return new PropertiesFileConfigurationSource();
    }

    public static Configuration resolve() {
        return new Configuration(resolveSource());
    }

    public URL getTestURL() throws ConfigurationException {
        String urlString = source.url();
        if (StringUtility.isEmpty(urlString))
            return null;
        try {
            if (urlString.endsWith("/"))
                urlString = urlString.substring(0, urlString.length() - 1);
            return new URL(urlString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.URL, urlString, e);
        }
    }

    public List<String> getBrowserFileNames() throws ConfigurationException {
        String browserFileNamesString = source.browserFileNames();
        try {
            return StringUtility.listFromCommaDelimitedString(browserFileNamesString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.BROWSER_FILE_NAMES, browserFileNamesString, e);
        }
    }

    public File getLogsDirectory() throws ConfigurationException {
        String logsDirectoryString = source.logsDirectory();
        try {
            if (StringUtility.isEmpty(logsDirectoryString))
                logsDirectoryString = ConfigurationProperty.LOGS_DIRECTORY.getDefaultValue();
            return new File(logsDirectoryString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.LOGS_DIRECTORY, logsDirectoryString, e);
        }
    }

    public int getPort() throws ConfigurationException {
        String portString = source.port();
        if (StringUtility.isEmpty(portString))
            portString = ConfigurationProperty.PORT.getDefaultValue();
        try {
            return Integer.parseInt(portString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.PORT, portString, e);
        }
    }

    public boolean shouldCloseBrowsersAfterTestRuns() throws ConfigurationException {
        String string = source.closeBrowsersAfterTestRuns();
        if (StringUtility.isEmpty(string))
            string = ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getDefaultValue();
        return Boolean.valueOf(string);
    }

    public File getResourceBase() throws ConfigurationException {
        String result = source.resourceBase();
        if (StringUtility.isEmpty(result))
            result = ConfigurationProperty.RESOURCE_BASE.getDefaultValue();
        String resourceBaseString = result;
        try {
            return new File(resourceBaseString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.RESOURCE_BASE, resourceBaseString, e);
        }
    }

    public ConfigurationSource getSource() {
        return source;
    }

    public boolean shouldLogStatus() {
        String logStatus = source.logStatus();
        if (StringUtility.isEmpty(logStatus))
            return true;
        return Boolean.valueOf(logStatus);
    }

    public Element asXml(ServerType serverType) {
        Element configurationElement = new Element("configuration");
        configurationElement.setAttribute("type", serverType.name());
        Element osElement = new Element("os");
        osElement.setText(SystemUtility.osString());
        configurationElement.addContent(osElement);
        for (ConfigurationProperty property : serverType.getRequiredAndOptionalConfigurationProperties())
            property.addXmlTo(configurationElement, this);
        return configurationElement;
    }

    public String[] asArgumentsArray() {
        List<ConfigurationProperty> properties = ConfigurationProperty.all();
        String[] arguments = new String[properties.size() * 2];
        int i = 0;
        for (ConfigurationProperty property : properties) {
            arguments[i++] = "-" + property.getName();
            arguments[i++] = property.getValueString(this);
        }
        return arguments;
    }

    public int getTimeoutSeconds() {
        String timeoutSecondsString = source.timeoutSeconds();
        if (StringUtility.isEmpty(timeoutSecondsString))
            timeoutSecondsString = ConfigurationProperty.TIMEOUT_SECONDS.getDefaultValue();
        try {
            return Integer.parseInt(timeoutSecondsString);
        } catch (NumberFormatException e) {
            throw new ConfigurationException(ConfigurationProperty.TIMEOUT_SECONDS, timeoutSecondsString, e);
        }
    }

    public List<URL> getRemoteMachineURLs() {
        String remoteMachineURLs = source.remoteMachineURLs();
        List<String> strings = StringUtility.listFromCommaDelimitedString(remoteMachineURLs);
        List<URL> result = new ArrayList<URL>(strings.size());
        for (String string : strings)
            try {
                if (string.endsWith("/"))
                    string = string.substring(0, string.length() - 1);
                result.add(new URL(string));
            } catch (MalformedURLException e) {
                throw new ConfigurationException(ConfigurationProperty.REMOTE_MACHINE_URLS, remoteMachineURLs, e);
            }
        return result;
    }

    public String getDescription() {
        return source.description();
    }

    public boolean isValidFor(ServerType type) {
        return type.getPropertiesInvalidFor(this).isEmpty();
    }

    public boolean shouldIgnoreUnresponsiveRemoteMachines() {
        String string = source.ignoreUnresponsiveRemoteMachines();
        if (StringUtility.isEmpty(string))
            string = ConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES.getDefaultValue();
        return Boolean.valueOf(string);
    }

    public String toString() {
        return getDescription() == null ? super.toString() : getDescription();
    }

}