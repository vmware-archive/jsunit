package net.jsunit.action;

import net.jsunit.XmlRenderable;
import net.jsunit.model.BrowserResult;

public class ResultAcceptorAction extends JsUnitAction implements BrowserResultAware {

	protected BrowserResult result;

	public String execute() throws Exception {
        runner.logStatus("Received submission");
		runner.accept(result);
        runner.logStatus("Done");
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
	
}