package net.jsunit.interceptor;

import net.jsunit.JsUnitFarmServer;
import net.jsunit.action.FarmTestRunnerAction;

import com.opensymphony.xwork.Action;

public class FarmServerInterceptor extends JsUnitInterceptor {

	protected void execute(Action targetAction) {
		FarmTestRunnerAction action = (FarmTestRunnerAction) targetAction;
		action.setFarmServer(JsUnitFarmServer.getFarmServerInstance());
	}

}
