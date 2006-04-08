package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.ServerRegistry;
import net.jsunit.action.JsUnitAggregateServerAware;

public class AggregateServerInterceptor extends JsUnitInterceptor {

    protected void execute(Action targetAction) {
        JsUnitAggregateServerAware action = (JsUnitAggregateServerAware) targetAction;
        action.setAggregateServer(ServerRegistry.getAggregateServer());
    }

}
