package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.RemoteMachineRunnerHitter;
import net.jsunit.action.RemoteRunnerHitterAware;

public class RemoteRunnerHitterInterceptor extends JsUnitInterceptor {

    protected void execute(Action targetAction) {
        RemoteRunnerHitterAware aware = ((RemoteRunnerHitterAware) targetAction);
        aware.setRemoteRunnerHitter(new RemoteMachineRunnerHitter());
    }

}
