package net.jsunit.configuration;

import net.jsunit.Utility;
import net.jsunit.XmlRenderable;

import org.jdom.Element;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public final class Configuration implements XmlRenderable {

    private ConfigurationSource source;
    public static final String DEFAULT_RESOURCE_BASE = ".";
    public static final int DEFAULT_PORT = 8080;
    public static final int DEFAULT_TIMEOUT_SECONDS = 60;

    public Configuration(ConfigurationSource source) {
        this.source = source;
    }

    public static Configuration resolve(String[] arguments) {
        ConfigurationSource source;
        if (arguments.length > 0)
            source = new ArgumentsConfigurationSource(Arrays.asList(arguments));
        else {
            source = resolveSource();
        }
        return new Configuration(source);
    }

    private static ConfigurationSource resolveSource() {
        EnvironmentVariablesConfigurationSource evConfig = new EnvironmentVariablesConfigurationSource();
        if (evConfig.isAppropriate())
            return evConfig;
        return new PropertiesFileConfigurationSource();
    }

    public static Configuration resolve() {
        return new Configuration(resolveSource());
    }

    public URL getTestURL() throws ConfigurationException {
        String urlString = source.url();
        try {
            return new URL(urlString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationConstants.URL, urlString, e);
        }
    }

    public List<String> getBrowserFileNames() throws ConfigurationException {
        String browserFileNamesString = source.browserFileNames();
        try {
            return Utility.listFromCommaDelimitedString(browserFileNamesString);
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationConstants.BROWSER_FILE_NAMES, browserFileNamesString, e);
        }
    }

    public File getLogsDirectory() throws ConfigurationException {
        String logsDirectoryString = source.logsDirectory();
        try {
            if (Utility.isEmpty(logsDirectoryString))
                logsDirectoryString = resourceBaseCheckForDefault() + File.separator + "logs";
            File logsDirectory = new File(logsDirectoryString);
            if (!logsDirectory.exists()) {
                logsDirectory.mkdir();
            }
            return logsDirectory;
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationConstants.LOGS_DIRECTORY, logsDirectoryString, e);
        }
    }

    public int getPort() throws ConfigurationException {
        String portString = source.port();
        try {
            int port;
            if (Utility.isEmpty(portString))
                port = DEFAULT_PORT;
            else
                port = Integer.parseInt(portString);
            return port;
        } catch (Exception e) {
            throw new ConfigurationException(ConfigurationConstants.PORT, portString, e);
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
            throw new ConfigurationException(ConfigurationConstants.RESOURCE_BASE, resourceBaseString, e);
        }
    }

    private String resourceBaseCheckForDefault() {
        String result = source.resourceBase();
        if (Utility.isEmpty(result))
            result = DEFAULT_RESOURCE_BASE;
        return result;
    }

    public ConfigurationSource getSource() {
        return source;
    }

    public boolean logStatus() {
    	String logStatus = source.logStatus();
    	if (Utility.isEmpty(logStatus))
    		return true;
    	return Boolean.valueOf(logStatus);
    }

    public Element asXml() {
        Element configuration = new Element("configuration");

        Element resourceBase = new Element(ConfigurationConstants.RESOURCE_BASE);
        resourceBase.setText(getResourceBase().toString());
        configuration.addContent(resourceBase);

        Element port = new Element(ConfigurationConstants.PORT);
        port.setText(String.valueOf(getPort()));
        configuration.addContent(port);

        Element logsDirectory = new Element(ConfigurationConstants.LOGS_DIRECTORY);
        logsDirectory.setText(getLogsDirectory().toString());
        configuration.addContent(logsDirectory);

        Element browserFileNames = new Element(ConfigurationConstants.BROWSER_FILE_NAMES);
        for (String name : getBrowserFileNames()) {
            Element browserFileName = new Element("browserFileName");
            browserFileName.setText(name);
            browserFileNames.addContent(browserFileName);
        }
        configuration.addContent(browserFileNames);

        Element url = new Element(ConfigurationConstants.URL);
        url.setText(getTestURL().toString());
        configuration.addContent(url);

        Element closeBrowsers = new Element(ConfigurationConstants.CLOSE_BROWSERS_AFTER_TEST_RUNS);
        closeBrowsers.setText(String.valueOf(shouldCloseBrowsersAfterTestRuns()));
        configuration.addContent(closeBrowsers);

        Element logStatus = new Element(ConfigurationConstants.LOG_STATUS);
        logStatus.setText(String.valueOf(logStatus()));
        configuration.addContent(logStatus);

        Element timeoutSeconds = new Element("timeoutSeconds");
        timeoutSeconds.setText(String.valueOf(getTimeoutSeconds()));
        configuration.addContent(timeoutSeconds);

        return configuration;
    }

	public String[] asArgumentsArray() {
		return new String[] {
			"-" + ConfigurationConstants.RESOURCE_BASE, getResourceBase().getAbsolutePath(),
			"-" + ConfigurationConstants.PORT, String.valueOf(getPort()),
			"-" + ConfigurationConstants.LOGS_DIRECTORY, getLogsDirectory().getAbsolutePath(),
			"-" + ConfigurationConstants.BROWSER_FILE_NAMES, commaSeparatedBrowserFileNames(),
			"-" + ConfigurationConstants.URL, getTestURL().toString(),
			"-" + ConfigurationConstants.CLOSE_BROWSERS_AFTER_TEST_RUNS, String.valueOf(shouldCloseBrowsersAfterTestRuns()),
			"-" + ConfigurationConstants.LOG_STATUS, String.valueOf(logStatus()),
			"-" + ConfigurationConstants.TIMEOUT_SECONDS, String.valueOf(getTimeoutSeconds())
		};
	}

	private String commaSeparatedBrowserFileNames() {
		StringBuffer result = new StringBuffer();
		for (Iterator it = getBrowserFileNames().iterator(); it.hasNext();) {
			result.append(it.next());
			if (it.hasNext())
				result.append(",");
		}
		return result.toString();
	}

	public int getTimeoutSeconds() {
		String timeoutSecondsString = source.timeoutSeconds();
		if (Utility.isEmpty(timeoutSecondsString))
			return DEFAULT_TIMEOUT_SECONDS;
		return Integer.parseInt(timeoutSecondsString);
	}

	public void ensureValid() {
        try {
            asArgumentsArray();
        } catch (ConfigurationException e) {
            throw e;
        }
    }
}