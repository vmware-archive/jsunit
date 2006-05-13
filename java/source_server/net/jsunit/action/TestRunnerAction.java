package net.jsunit.action;

import net.jsunit.SkinSource;
import net.jsunit.TestRunManager;
import net.jsunit.XmlRenderable;
import net.jsunit.configuration.Configuration;
import net.jsunit.model.Browser;

import java.util.List;

public class TestRunnerAction
        extends JsUnitBrowserTestRunnerAction
        implements RequestSourceAware, TestPageURLAware, BrowserSelectionAware {

    public static final String TRANSFORM = "transform";

    private TestRunManager manager;
    private String url;
    private String remoteIpAddress;
    private String remoteHost;
    private String invalidBrowserId;
    private List<Browser> selectedBrowsers;
    private String referrer;

    public String execute() throws Exception {
        long startTime = System.currentTimeMillis();
        runner.logStatus(requestReceivedMessage());
        //noinspection SynchronizeOnNonFinalField
        synchronized (runner) {
            manager = new TestRunManager(runner, url);
            if (selectedBrowsers != null)
                manager.limitToBrowsers(selectedBrowsers);
            manager.runTests();
        }
        runner.logStatus("Done running tests (" + ((System.currentTimeMillis() - startTime) / 1000d) + ") seconds)");
        return SUCCESS;
    }

    private String requestReceivedMessage() {
        return new RequestReceivedMessage(remoteHost, remoteIpAddress, url).generateMessage();
    }

    public XmlRenderable getXmlRenderable() {
        if (invalidBrowserId != null)
            return new ErrorXmlRenderable("Invalid browser ID: " + invalidBrowserId);
        return manager.getTestRunResult();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Configuration getServerConfiguration() {
        return runner.getConfiguration();
    }

    public void setRequestIPAddress(String ipAddress) {
        remoteIpAddress = ipAddress;
    }

    public void setRequestHost(String host) {
        remoteHost = host;
    }

    public void setInvalidBrowserId(String invalidId) {
        this.invalidBrowserId = invalidId;
    }

    public void setSelectedBrowsers(List<Browser> browsers) {
        this.selectedBrowsers = browsers;
    }

    public SkinSource getSkinSource() {
        return runner;
    }

    public String getUrl() {
        return url;
    }

    public String getRequestIpAddress() {
        return this.remoteIpAddress;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public Browser getBrowserById(int id) {
        return getServerConfiguration().getBrowserById(id);
    }

    public List<Browser> getAllBrowsers() {
        return getServerConfiguration().getAllBrowsers();
    }

}
