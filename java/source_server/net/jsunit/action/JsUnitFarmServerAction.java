package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.RemoteRunnerHitter;

public abstract class JsUnitFarmServerAction
        implements  Action,
                    XmlProducer,
                    RemoteRunnerHitterAware,
                    JsUnitServerAware {

	protected JsUnitFarmServer server;
	protected RemoteRunnerHitter hitter;	
	
	public void setFarmServer(JsUnitFarmServer server) {
		this.server = server;
	}

	public void setRemoteRunnerHitter(RemoteRunnerHitter hitter) {
		this.hitter  = hitter;
	}

}
