package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.ServerRegistry;
import net.jsunit.action.JsUnitFarmServerAware;

public class FarmServerInterceptor extends JsUnitInterceptor {

    protected void execute(Action targetAction) {
        JsUnitFarmServerAware action = (JsUnitFarmServerAware) targetAction;
        action.setFarmServer(ServerRegistry.getFarmServer());
    }

}
