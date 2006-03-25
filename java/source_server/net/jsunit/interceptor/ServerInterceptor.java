package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.ServerRegistry;
import net.jsunit.action.ServerStatusAction;

public class ServerInterceptor extends JsUnitInterceptor {
    protected void execute(Action targetAction) {
        ServerStatusAction action = (ServerStatusAction) targetAction;
        action.setJsUnitServer(ServerRegistry.getServer());
    }
}
