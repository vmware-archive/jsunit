package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.ServerConfigurationProperty;
import org.jdom.Document;
import org.jdom.Element;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class RemoteConfigurationSource implements ConfigurationSource {

    private Document document;

    public RemoteConfigurationSource(RemoteServerHitter hitter, String remoteMachineURL) throws IOException {
        document = hitter.hitURL(new URL(remoteMachineURL + "/config"));
    }

    public boolean isInitialized() {
        return document != null;
    }

    public String browserFileNames() {
        return commaSeparatedTextOfChildrenOfElement(ServerConfigurationProperty.BROWSER_FILE_NAMES);
    }

    public String closeBrowsersAfterTestRuns() {
        return textOfElement(ServerConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName());
    }

    public String description() {
        return textOfElement(ServerConfigurationProperty.DESCRIPTION.getName());
    }

    public String logsDirectory() {
        return textOfElement(ServerConfigurationProperty.LOGS_DIRECTORY.getName());
    }

    public String port() {
        return textOfElement(ServerConfigurationProperty.PORT.getName());
    }

    public String remoteMachineURLs() {
        return commaSeparatedTextOfChildrenOfElement(ServerConfigurationProperty.REMOTE_MACHINE_URLS);
    }

    public String resourceBase() {
        return textOfElement(ServerConfigurationProperty.RESOURCE_BASE.getName());
    }

    public String timeoutSeconds() {
        return textOfElement(ServerConfigurationProperty.TIMEOUT_SECONDS.getName());
    }

    public String url() {
        return textOfElement(ServerConfigurationProperty.URL.getName());
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return textOfElement(ServerConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES.getName());
    }

    protected String textOfElement(String elementName) {
        Element element = document.getRootElement().getChild(elementName);
        if (element == null)
            return "";
        return element.getTextTrim();
    }

    private String commaSeparatedTextOfChildrenOfElement(ServerConfigurationProperty property) {
        Element parent = document.getRootElement().getChild(property.getName());
        if (parent == null)
            return "";
        //noinspection unchecked
        List<Element> children = (List<Element>) parent.getChildren();
        StringBuffer buffer = new StringBuffer();
        for (Iterator<Element> it = children.iterator(); it.hasNext();) {
            Element child = it.next();
            buffer.append(child.getTextTrim());
            if (it.hasNext())
                buffer.append(",");
        }
        return buffer.toString();
    }

    public String osString() {
        return textOfElement("os");
    }

    public String ipAddress() {
        return textOfElement("ipAddress");
    }

    public String hostname() {
        return textOfElement("hostname");
    }

    public String serverType() {
        return document.getRootElement().getAttributeValue("type");
    }

}
