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
    private boolean needsLogging = true;
    public static final String DEFAULT_RESOURCE_BASE = ".";
    public static final int DEFAULT_PORT = 8080;

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
                Utility.log("Creating logs directory " + logsDirectory, false);
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
                port = Configuration.DEFAULT_PORT;
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
            result = Configuration.DEFAULT_RESOURCE_BASE;
        return result;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(ConfigurationConstants.PORT).append(": ").append(getPort()).append("\n");
        result.append(ConfigurationConstants.RESOURCE_BASE).append(": ").append(getResourceBase().getAbsolutePath()).append("\n");
        result.append(ConfigurationConstants.LOGS_DIRECTORY).append(": ").append(getLogsDirectory().getAbsolutePath()).append("\n");
        result.append(ConfigurationConstants.BROWSER_FILE_NAMES).append(": ").append(getBrowserFileNames()).append("\n");
        result.append(ConfigurationConstants.URL).append(": ").append(getTestURL());
        return result.toString();
    }

    public ConfigurationSource getSource() {
        return source;
    }

    public boolean needsLogging() {
        return needsLogging;
    }

    public void setNeedsLogging(boolean b) {
        needsLogging = b;
    }

    public Element asXml() {
        Element configuration = new Element("configuration");

        Element resourceBase = new Element("resourceBase");
        resourceBase.setText(getResourceBase().toString());
        configuration.addContent(resourceBase);

        Element port = new Element("port");
        port.setText(String.valueOf(getPort()));
        configuration.addContent(port);

        Element logsDirectory = new Element("logsDirectory");
        logsDirectory.setText(getLogsDirectory().toString());
        configuration.addContent(logsDirectory);

        Element browserFileNames = new Element("browserFileNames");
        for (String name : getBrowserFileNames()) {
            Element browserFileName = new Element("browserFileName");
            browserFileName.setText(name);
            browserFileNames.addContent(browserFileName);
        }
        configuration.addContent(browserFileNames);

        Element url = new Element("url");
        url.setText(getTestURL().toString());
        configuration.addContent(url);

        Element closeBrowsers = new Element("closeBrowsersAfterTestRuns");
        closeBrowsers.setText(String.valueOf(shouldCloseBrowsersAfterTestRuns()));
        configuration.addContent(closeBrowsers);

        return configuration;
    }

	public String[] asArgumentsArray() {
		return new String[] {
			"-" + ConfigurationConstants.RESOURCE_BASE, getResourceBase().getAbsolutePath(),
			"-" + ConfigurationConstants.PORT, String.valueOf(getPort()),
			"-" + ConfigurationConstants.LOGS_DIRECTORY, getLogsDirectory().getAbsolutePath(),
			"-" + ConfigurationConstants.BROWSER_FILE_NAMES, commaSeparatedBrowserFileNames(),
			"-" + ConfigurationConstants.URL, getTestURL().toString(),
			"-" + ConfigurationConstants.CLOSE_BROWSERS_AFTER_TEST_RUNS, String.valueOf(shouldCloseBrowsersAfterTestRuns())
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
}