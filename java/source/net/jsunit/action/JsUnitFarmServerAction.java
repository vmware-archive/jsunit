package net.jsunit.action;

import net.jsunit.JsUnitFarmServer;
import net.jsunit.RemoteRunnerHitter;

import com.opensymphony.xwork.Action;

public abstract class JsUnitFarmServerAction implements Action, XmlProducer {

	protected JsUnitFarmServer server;
	protected RemoteRunnerHitter hitter;	
	
	public void setFarmServer(JsUnitFarmServer server) {
		this.server = server;
	}

	public void setRemoteRunnerHitter(RemoteRunnerHitter hitter) {
		this.hitter  = hitter;
	}


}
