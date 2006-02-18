package net.jsunit.action;

import net.jsunit.FarmTestRunManager;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.RemoteMachineRunnerHitter;
import net.jsunit.RemoteRunnerHitter;
import net.jsunit.XmlRenderable;

import com.opensymphony.xwork.Action;

public class FarmTestRunnerAction implements Action, XmlProducer {

	private JsUnitFarmServer server;
	private FarmTestRunManager manager;
	private RemoteRunnerHitter hitter = new RemoteMachineRunnerHitter();
    private String overrideURL;

    public String execute() throws Exception {
        String message = "Received request to run farm tests";
        if (overrideURL != null)
            message += " with URL " + overrideURL;
        server.logStatus(message);
        manager = new FarmTestRunManager(hitter, server.getConfiguration(), overrideURL);
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

	public void setRemoteRunnerHitter(RemoteRunnerHitter hitter) {
		this.hitter  = hitter;
	}

    public void setUrl(String overrideURL) {
        this.overrideURL = overrideURL;
    }
}
