package net.jsunit.action;

import net.jsunit.TestRunManager;
import net.jsunit.XmlRenderable;
import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.model.Browser;

import java.util.List;

public class TestRunnerAction
        extends JsUnitBrowserTestRunnerAction
        implements RequestSourceAware, TestPageURLAware, BrowserSelectionAware, InvalidTestRunAttemptAware {

    private TestRunManager manager;
    private String url;
    private String remoteIpAddress;
    private String remoteHost;
    private String errorMessage;
    private List<Browser> selectedBrowsers;
    private String referrer;

    public String execute() throws Exception {
        if (errorMessage == null) {
            long startTime = System.currentTimeMillis();
            logger.info(requestReceivedMessage());
            //noinspection SynchronizeOnNonFinalField
            synchronized (runner) {
                manager = new TestRunManager(runner, url);
                if (selectedBrowsers != null)
                    manager.limitToBrowsers(selectedBrowsers);
                manager.runTests();
            }
            logger.info("Done running tests (" + ((System.currentTimeMillis() - startTime) / 1000d) + ") seconds)");
        }
        return SUCCESS;
    }

    private String requestReceivedMessage() {
        return new RequestReceivedMessage(remoteHost, remoteIpAddress, url).generateMessage();
    }

    public XmlRenderable getXmlRenderable() {
        if (errorMessage != null)
            return new ErrorXmlRenderable(errorMessage);
        return manager.getTestRunResult();
    }

    public void setUrl(String url) {
        this.url = url.trim();
    }

    public ServerConfiguration getConfiguration() {
        return runner.getConfiguration();
    }

    public void setRequestIPAddress(String ipAddress) {
        remoteIpAddress = ipAddress;
    }

    public void setRequestHost(String host) {
        remoteHost = host;
    }

    public void setSelectedBrowsers(List<Browser> browsers) {
        this.selectedBrowsers = browsers;
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
        return getConfiguration().getBrowserById(id);
    }

    public List<Browser> getAllBrowsers() {
        return getConfiguration().getAllBrowsers();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
