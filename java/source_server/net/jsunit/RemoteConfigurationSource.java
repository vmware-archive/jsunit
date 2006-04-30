package net.jsunit;

import net.jsunit.configuration.ConfigurationProperty;
import net.jsunit.configuration.ConfigurationSource;
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
        return commaSeparatedTextOfChildrenOfElement(ConfigurationProperty.BROWSER_FILE_NAMES);
    }

    public String closeBrowsersAfterTestRuns() {
        return textOfElement(ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS);
    }

    public String description() {
        return textOfElement(ConfigurationProperty.DESCRIPTION);
    }

    public String logsDirectory() {
        return textOfElement(ConfigurationProperty.LOGS_DIRECTORY);
    }

    public String port() {
        return textOfElement(ConfigurationProperty.PORT);
    }

    public String remoteMachineURLs() {
        return commaSeparatedTextOfChildrenOfElement(ConfigurationProperty.REMOTE_MACHINE_URLS);
    }

    public String resourceBase() {
        return textOfElement(ConfigurationProperty.RESOURCE_BASE);
    }

    public String timeoutSeconds() {
        return textOfElement(ConfigurationProperty.TIMEOUT_SECONDS);
    }

    public String url() {
        return textOfElement(ConfigurationProperty.URL);
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return textOfElement(ConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES);
    }

    private String textOfElement(ConfigurationProperty property) {
        return textOfElement(property.getName());
    }

    private String textOfElement(String elementName) {
        Element element = document.getRootElement().getChild(elementName);
        if (element == null)
            return "";
        return element.getTextTrim();
    }

    private String commaSeparatedTextOfChildrenOfElement(ConfigurationProperty property) {
        Element parent = document.getRootElement().getChild(property.getName());
        if (parent == null)
            return "";
        //noinspection unchecked
        List<Element> children = parent.getChildren();
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

    public String useCaptcha() {
        return textOfElement(ConfigurationProperty.USE_CAPTCHA);
    }

}
