package net.jsunit.model;

import net.jsunit.XmlRenderable;
import net.jsunit.utility.SystemUtility;
import org.jdom.Document;
import org.jdom.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TestRunResult extends AbstractResult implements XmlRenderable, Comparable<TestRunResult>, Iterable<BrowserResult> {

    public static final String NAME = "testRunResult";

    private List<BrowserResult> browserResults = new ArrayList<BrowserResult>();
    private String url;
    private String osName;
    private String ipAddress;
    private String hostname;
    private boolean unresponsive = false;

    public TestRunResult() {
    }

    public TestRunResult(String url) {
        this.url = url;
    }

    public TestRunResult(URL baseURL) {
        this(baseURL.toString());
    }

    public void addBrowserResult(BrowserResult browserResult) {
        browserResults.add(browserResult);
    }

    public Element asXml() {
        Element root = new Element(NAME);
        root.setAttribute("type", _getResultType().name());
        if (url != null)
            root.setAttribute("url", url);
        if (hasPlatformType())
            addPlatformType(root);
        if (hasProperties()) {
            Element properties = new Element("properties");
            addProperties(properties);
            root.addContent(properties);
        }
        for (BrowserResult browserResult : browserResults)
            root.addContent(browserResult.asXml());
        return root;
    }

    private void addPlatformType(Element root) {
        PlatformType type = PlatformType.resolve(osName);
        root.addContent(type.asXml());
    }

    private boolean hasPlatformType() {
        return osName != null;
    }

    private boolean hasProperties() {
        return osName != null || ipAddress != null || hostname != null;
    }

    private void addProperties(Element element) {
        if (osName != null)
            addProperty(element, "os", osName);
        if (ipAddress != null)
            addProperty(element, "ipAddress", ipAddress);
        if (hostname != null)
            addProperty(element, "hostname", hostname);
    }

    private void addProperty(Element element, String name, String value) {
        Element property = new Element("property");
        property.setAttribute("name", name);
        property.setAttribute("value", value);
        element.addContent(property);
    }

    protected List<? extends Result> getChildren() {
        return browserResults;
    }

    public void setUnresponsive() {
        unresponsive = true;
    }

    public boolean wasUnresponsive() {
        return unresponsive;
    }

    public String getUrl() {
        return url;
    }

    public ResultType _getResultType() {
        if (unresponsive)
            return ResultType.UNRESPONSIVE;
        else
            return super._getResultType();
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOsName() {
        return osName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getHostname() {
        return hostname;
    }

    public int compareTo(TestRunResult other) {
        if (url == null || other.getUrl() == null)
            return 0;
        return url.compareTo(other.getUrl());
    }

    public void initializeProperties() {
        setOsName(SystemUtility.osString());
        setHostname(SystemUtility.hostname());
        setIpAddress(SystemUtility.ipAddress());
    }

    protected void addMyErrorStringTo(StringBuffer buffer) {
        buffer.append("  ");
        buffer.append(getDisplayString());
        buffer.append("\n");
    }

    protected void addChildErrorStringTo(Result child, StringBuffer buffer) {
        if (!child.wasSuccessful()) {
            child.addErrorStringTo(buffer);
            if (url != null) {
                if (!buffer.toString().endsWith("\n")) {
                    buffer.append("\n");
                }
                buffer.append("      The result log is at ");
                BrowserResult childBrowserResult = (BrowserResult) child;
                buffer.append(childBrowserResult.getLogUrl(url));
            }
        }
    }

    public String getDisplayString() {
        boolean hasURL = url != null;
        StringBuffer buffer = new StringBuffer();
        if (hasURL)
            buffer.append(url);
        else
            buffer.append("localhost");
        buffer.append(" (IP address: ");
        if (ipAddress != null)
            buffer.append(ipAddress);
        else
            buffer.append("unknown");
        buffer.append(", host name: ");
        if (hostname != null)
            buffer.append(hostname);
        else buffer.append("unknown");
        buffer.append(", OS: ");
        if (osName != null)
            buffer.append(osName);
        else buffer.append("unknown");
        buffer.append(")");
        return buffer.toString();
    }

    public List<BrowserResult> _getBrowserResults() {
        return browserResults;
    }

    public BrowserResult[] getBrowserResults() {
        return browserResults.toArray(new BrowserResult[browserResults.size()]);
    }

    public void setBrowserResults(BrowserResult[] results) {
        browserResults = Arrays.asList(results);
    }

    public Document asXmlDocument() {
        return new Document(asXml());
    }

    public boolean hasPlatformType(PlatformType platformType) {
        return getOsName() != null && getOsName().toLowerCase().indexOf(platformType.getDisplayName().toLowerCase()) != -1;
    }

    public Iterator<BrowserResult> iterator() {
        return _getBrowserResults().iterator();
    }

    public BrowserResult findBrowserResultMatching(BrowserSpecification spec) {
        for (BrowserResult browserResult : this)
            if (spec.matches(browserResult))
                return browserResult;
        return null;
    }
}
