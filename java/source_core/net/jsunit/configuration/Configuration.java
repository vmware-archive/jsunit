package net.jsunit.configuration;

import net.jsunit.Utility;
import net.jsunit.XmlRenderable;
import org.jdom.Element;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Configuration implements XmlRenderable {

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
        if (Utility.isEmpty(urlString))
            return null;
        try {
            if (urlString.endsWith("/"))
                urlString = urlString.substring(0, urlString.length()-1);
            return new URL(urlString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.URL, urlString, e);
        }
    }

    public List<String> getBrowserFileNames() throws ConfigurationException {
        String browserFileNamesString = source.browserFileNames();
        try {
            return Utility.listFromCommaDelimitedString(browserFileNamesString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.BROWSER_FILE_NAMES, browserFileNamesString, e);
        }
    }

    public File getLogsDirectory() throws ConfigurationException {
        String logsDirectoryString = source.logsDirectory();
        try {
            if (Utility.isEmpty(logsDirectoryString))
                logsDirectoryString = ConfigurationProperty.LOGS_DIRECTORY.getDefaultValue();
            return new File(logsDirectoryString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.LOGS_DIRECTORY, logsDirectoryString, e);
        }
    }

    public int getPort() throws ConfigurationException {
        String portString = source.port();
        if (Utility.isEmpty(portString))
            portString = ConfigurationProperty.PORT.getDefaultValue();
        try {
            return Integer.parseInt(portString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.PORT, portString, e);
        }
    }

    public boolean shouldCloseBrowsersAfterTestRuns() throws ConfigurationException {
        String string = source.closeBrowsersAfterTestRuns();
        if (Utility.isEmpty(string))
            return true;
        return Boolean.valueOf(string);
    }

    public File getResourceBase() throws ConfigurationException {
        String resourceBaseString = resourceBaseCheckForDefault();
        try {
            return new File(resourceBaseString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationProperty.RESOURCE_BASE, resourceBaseString, e);
        }
    }

    private String resourceBaseCheckForDefault() {
        String result = source.resourceBase();
        if (Utility.isEmpty(result))
            result = ConfigurationProperty.RESOURCE_BASE.getDefaultValue();
        return result;
    }

    public ConfigurationSource getSource() {
        return source;
    }

    public boolean shouldLogStatus() {
        String logStatus = source.logStatus();
        if (Utility.isEmpty(logStatus))
            return true;
        return Boolean.valueOf(logStatus);
    }

    public Element asXml() {
        Element configurationElement = new Element("configuration");

        for (ConfigurationProperty property : ConfigurationProperty.values()) {
        	property.addXmlTo(configurationElement, this);
        }
        
        return configurationElement;
        
    }

    public String[] asArgumentsArray() {
    	ConfigurationProperty[] properties = ConfigurationProperty.values();
		String[] arguments = new String[properties.length * 2];
    	int i = 0;
    	for (ConfigurationProperty property : properties) {
    		arguments[i++] = "-" + property.getName();
    		arguments[i++] = property.getValueString(this);
    	}
    	return arguments;
    }

    public int getTimeoutSeconds() {
        String timeoutSecondsString = source.timeoutSeconds();
        if (Utility.isEmpty(timeoutSecondsString))
            timeoutSecondsString = ConfigurationProperty.TIMEOUT_SECONDS.getDefaultValue();
        return Integer.parseInt(timeoutSecondsString);
    }

    public List<ConfigurationProperty> getPropertiesInvalidFor(ConfigurationType type) {
    	List<ConfigurationProperty> result = new ArrayList<ConfigurationProperty>();

        for (ConfigurationProperty property : type.getRequiredConfigurationProperties()) {
            if (Utility.isEmpty(property.getValueString(this)))
                result.add(property);
        }

        List<ConfigurationProperty> propertiesInQuestion = new ArrayList<ConfigurationProperty>();
        propertiesInQuestion.addAll(type.getRequiredConfigurationProperties());
        propertiesInQuestion.addAll(type.getOptionalConfigurationProperties());

        for (ConfigurationProperty property : propertiesInQuestion) {
			try {
				property.getValueString(this);
			} catch (ConfigurationException e){
				result.add(property);
			}
        }
    	return result;
    }

    public List<URL> getRemoteMachineURLs() {
        String remoteMachineURLs = source.remoteMachineURLs();
        List<String> strings = Utility.listFromCommaDelimitedString(remoteMachineURLs);
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

	public boolean isValidFor(ConfigurationType type) {
		return getPropertiesInvalidFor(type).isEmpty();
	}

}