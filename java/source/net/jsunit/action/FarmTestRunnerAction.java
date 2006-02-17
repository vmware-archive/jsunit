package net.jsunit.action;

import net.jsunit.FarmTestRunManager;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.RemoteMachineRunnerHitter;
import net.jsunit.RemoteRunnerHitter;
import net.jsunit.XmlRenderable;
import net.jsunit.action.FarmTestRunnerActionTest.SuccessfulRemoteRunnerHitter;

import com.opensymphony.xwork.Action;

public class FarmTestRunnerAction implements Action, XmlProducer {

	private JsUnitFarmServer server;
	private FarmTestRunManager manager;
	private RemoteRunnerHitter hitter = new RemoteMachineRunnerHitter();

	public String execute() throws Exception {
		server.logStatus("Received request to run farm tests");
		manager = new FarmTestRunManager(hitter, server.getConfiguration());
		manager.runTests();
		server.logStatus("Done running farm tests");		
		return SUCCESS;
	}

	public XmlRenderable getXmlRenderable() {
		return manager.getTestRunResult();
	}

	public void setFarmServer(JsUnitFarmServer server) {
		this.server = server;
	}
	
	public FarmTestRunManager getTestRunManager() {
		return manager;
	}

	public void setRemoteRunnerHitter(SuccessfulRemoteRunnerHitter hitter) {
		this.hitter  = hitter;
	}

}
