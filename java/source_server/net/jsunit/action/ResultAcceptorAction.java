package net.jsunit.action;

import net.jsunit.XmlRenderable;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;

public class ResultAcceptorAction extends JsUnitBrowserTestRunnerAction implements BrowserResultAware, RequestSourceAware {

    protected BrowserResult result;
    private String ipAddress;

    public String execute() throws Exception {
        runner.logStatus("Received submission from browser " + result.getBrowser().getDisplayName());
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
        return runner.getConfiguration().getBrowserById(id);
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