package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.ServerRegistry;
import net.jsunit.action.JsUnitServerAware;

public class ServerInterceptor extends JsUnitInterceptor {

    protected void execute(Action targetAction) {
        JsUnitServerAware action = (JsUnitServerAware) targetAction;
        action.setJsUnitServer(ServerRegistry.getServer());
    }

}
