package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitServer;
import net.jsunit.action.JsUnitServerAware;

public class ServerInterceptor extends JsUnitInterceptor {

    protected void execute(Action targetAction) throws Exception {
        JsUnitServerAware action = (JsUnitServerAware) targetAction;
        action.setJsUnitServer(JsUnitServer.instance());
    }

}
