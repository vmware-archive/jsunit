package net.jsunit.interceptor;

import net.jsunit.RemoteMachineRunnerHitter;
import net.jsunit.action.RemoteRunnerHitterAware;

import com.opensymphony.xwork.Action;

public class RemoteRunnerHitterInterceptor extends JsUnitInterceptor {

	protected void execute(Action targetAction) {
		RemoteRunnerHitterAware aware = ((RemoteRunnerHitterAware) targetAction);
		aware.setRemoteRunnerHitter(new RemoteMachineRunnerHitter());
	}

}
