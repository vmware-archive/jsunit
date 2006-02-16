package net.jsunit.action;

import net.jsunit.FarmTestRunManager;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.XmlRenderable;

import com.opensymphony.xwork.Action;

public class FarmTestRunnerAction implements Action, XmlProducer {

	private JsUnitFarmServer server;
	private FarmTestRunManager manager;

	public String execute() throws Exception {
		server.logStatus("Received request to run farm tests");
		manager = new FarmTestRunManager(server.getConfiguration());
		manager.runTests();
		server.logStatus("Done with to run farm tests");		
		return SUCCESS;
	}

	public XmlRenderable getXmlRenderable() {
		return manager.getTestRunResult();
	}

	public void setFarmServer(JsUnitFarmServer server) {
		this.server = server;
	}

}
