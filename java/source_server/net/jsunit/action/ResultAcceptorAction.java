package net.jsunit.action;

import net.jsunit.XmlRenderable;
import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;

import java.util.List;

public class ResultAcceptorAction extends JsUnitBrowserTestRunnerAction implements BrowserResultAware, RequestSourceAware {

    protected BrowserResult result;
    private String ipAddress;

    public String execute() throws Exception {
        logger.info("Received submission from browser " + result.getBrowser().getDisplayName());
        runner.accept(result);
        return SUCCESS;
    }

    public void setBrowserResult(BrowserResult result) {
        this.result = result;
    }

    public BrowserResult getResult() {
        return result;
    }

    public XmlRenderable getXmlRenderable() {
        return getResult();
    }

    public Browser getBrowserById(int id) {
        return configuration().getBrowserById(id);
    }

    public List<Browser> getAllBrowsers() {
        return configuration().getAllBrowsers();
    }

    private ServerConfiguration configuration() {
        return runner.getConfiguration();
    }

    public void setRequestIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setRequestHost(String host) {
    }

    public String getRequestIpAddress() {
        return ipAddress;
    }

    public void setReferrer(String referrer) {
    }
}