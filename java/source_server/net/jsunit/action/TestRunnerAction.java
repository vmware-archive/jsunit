package net.jsunit.action;

import net.jsunit.TestRunManager;
import net.jsunit.XmlRenderable;

public class TestRunnerAction extends JsUnitBrowserTestRunnerAction {

    private TestRunManager manager;
    private String url;
    private String remoteAddress;

    public String execute() throws Exception {
        String message = "Received request to run tests";
        if (remoteAddress != null)
            message += " from " + remoteAddress;
        runner.logStatus(message);
        synchronized (runner) {
            manager = new TestRunManager(runner, url);
            manager.runTests();
        }
        runner.logStatus("Done running tests");
        return SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
    	return manager.getTestRunResult();
    }

	public void setUrl(String url) {
		this.url = url;
	}

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

}
