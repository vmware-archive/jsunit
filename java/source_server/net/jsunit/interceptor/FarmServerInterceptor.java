package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.action.FarmServerAware;

public class FarmServerInterceptor extends JsUnitInterceptor {

	protected void execute(Action targetAction) {
		FarmServerAware action = (FarmServerAware) targetAction;
		action.setFarmServer(JsUnitFarmServer.farmServerInstance());
	}

}
