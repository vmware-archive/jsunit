package net.jsunit.action;

import net.jsunit.XmlRenderable;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.Browser;

public class ResultAcceptorAction extends JsUnitBrowserTestRunnerAction implements BrowserResultAware {

    protected BrowserResult result;

    public String execute() throws Exception {
        runner.logStatus("Received submission");
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
}