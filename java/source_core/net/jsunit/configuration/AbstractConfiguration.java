package net.jsunit.configuration;

import net.jsunit.model.PlatformType;
import org.jdom.Document;
import org.jdom.Element;

import java.net.URL;
import java.util.List;

public abstract class AbstractConfiguration implements Configuration {
    private String osString;
    private String ipAddress;
    private String hostname;
    private int port;
    private List<URL> remoteMachineURLs;
    private boolean ignoreUnresponsiveRemoteMachines;
    private URL testURL;

    protected AbstractConfiguration(ConfigurationSource source) {
        osString = source.osString();
        ipAddress = source.ipAddress();
        hostname = source.hostname();
    }

    public Element asXml() {
        Element configurationElement = new Element("configuration");
        configurationElement.setAttribute("type", getServerType().name());
        addSystemElementsTo(configurationElement);
        addPropertiesTo(configurationElement);
        return configurationElement;
    }

    protected abstract void addPropertiesTo(Element element);

    public abstract ServerType getServerType();

    private void addSystemElementsTo(Element element) {
        Element osElement = new Element("os");
        osElement.setText(osString);
        element.addContent(osElement);
        Element ipAddressElement = new Element("ipAddress");
        ipAddressElement.setText(ipAddress);
        element.addContent(ipAddressElement);
        Element hostnameElement = new Element("hostname");
        hostnameElement.setText(hostname);
        element.addContent(hostnameElement);
    }

    public String getSystemDisplayString() {
        return hostname + " (" + ipAddress + "), " + osString;
    }

    public String getOsString() {
        return osString;
    }

    public PlatformType getPlatformType() {
        return PlatformType.resolve(osString);
    }

    public boolean hasPlatformType(PlatformType platformType) {
        return platformType == getPlatformType();
    }

    public Document asXmlDocument() {
        return new Document(asXml());
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<URL> getRemoteMachineURLs() {
        return remoteMachineURLs;
    }

    public void setRemoteMachineURLs(List<URL> remoteMachineURLs) {
        this.remoteMachineURLs = remoteMachineURLs;
    }

    public boolean shouldIgnoreUnresponsiveRemoteMachines() {
        return ignoreUnresponsiveRemoteMachines;
    }

    public void setIgnoreUnresponsiveRemoteMachines(boolean ignoreUnresponsiveRemoteMachines) {
        this.ignoreUnresponsiveRemoteMachines = ignoreUnresponsiveRemoteMachines;
    }

    public URL getTestURL() {
        return testURL;
    }

    public void setTestURL(URL url) {
        this.testURL = url;
    }
}
