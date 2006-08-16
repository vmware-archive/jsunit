package net.jsunit.configuration;

import net.jsunit.model.Browser;
import net.jsunit.model.BrowserSource;
import net.jsunit.model.BrowserSpecification;
import org.jdom.Element;

import java.io.File;
import java.util.List;

public class ServerConfiguration extends AbstractConfiguration implements BrowserSource {

    private List<Browser> browsers;
    private boolean closeBrowsersAfterTestRuns;
    private String description;
    private File logsDirectory;
    private File resourceBase;
    private int timeoutSeconds;

    public ServerConfiguration(ConfigurationSource source) {
        super(source);
        for (ServerConfigurationProperty property : ServerConfigurationProperty.values())
            property.configure(this, source);
    }

    public ServerType getServerType() {
        return ServerType.SERVER;
    }

    protected void addPropertiesTo(Element configurationElement) {
        for (ServerConfigurationProperty property : ServerConfigurationProperty.all())
            property.addXmlTo(configurationElement, this);
    }

    public String[] asArgumentsArray() {
        List<ServerConfigurationProperty> properties = ServerConfigurationProperty.all();
        String[] arguments = new String[properties.size() * 2];
        int i = 0;
        for (ServerConfigurationProperty property : properties) {
            arguments[i++] = "-" + property.getName();
            arguments[i++] = property.getValueString(this);
        }
        return arguments;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (ServerConfigurationProperty property : ServerConfigurationProperty.values()) {
            buffer.append(property.toString(this)).append("\r\n");
        }
        return buffer.toString();
    }

    public List<Browser> getBrowsers() {
        return browsers;
    }

    public void setBrowsers(List<Browser> browsers) {
        this.browsers = browsers;
    }

    public boolean shouldCloseBrowsersAfterTestRuns() {
        return closeBrowsersAfterTestRuns;
    }

    public void setCloseBrowsersAfterTestRuns(boolean closeBrowsersAfterTestRuns) {
        this.closeBrowsersAfterTestRuns = closeBrowsersAfterTestRuns;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getLogsDirectory() {
        return logsDirectory;
    }

    public void setLogsDirectory(File logsDirectory) {
        this.logsDirectory = logsDirectory;
    }

    public File getResourceBase() {
        return resourceBase;
    }

    public void setResourceBase(File resourceBase) {
        this.resourceBase = resourceBase;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public Browser getBrowserById(int id) {
        for (Browser browser : browsers)
            if (browser.hasId(id))
                return browser;
        return null;
    }

    public List<Browser> getAllBrowsers() {
        return browsers;
    }

    public boolean equals(ServerConfiguration other) {
        for (ServerConfigurationProperty property : ServerConfigurationProperty.all()) {
            String myValue = property.getValueString(this);
            String otherValue = property.getValueString(other);
            if (!myValue.equals(otherValue))
                return false;
        }
        return true;
    }

    public Browser getBrowserMatching(BrowserSpecification spec) {
        for (Browser browser : browsers) {
            if (spec.matches(browser))
                return browser;
        }
        return null;
    }

}
