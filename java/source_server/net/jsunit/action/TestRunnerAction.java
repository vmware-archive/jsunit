package net.jsunit.action;

import net.jsunit.TestRunManager;
import net.jsunit.XmlRenderable;

public class TestRunnerAction extends JsUnitBrowserTestRunnerAction {

    private TestRunManager manager;
    private String url;

    public String execute() throws Exception {
        runner.logStatus("Received request to run tests");
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

}
