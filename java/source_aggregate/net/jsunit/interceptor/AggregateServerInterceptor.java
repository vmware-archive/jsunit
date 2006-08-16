package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.action.JsUnitAggregateServerAware;

public class AggregateServerInterceptor extends JsUnitInterceptor {

    protected void execute(Action targetAction) throws Exception {
        if (targetAction instanceof JsUnitAggregateServerAware) {
            JsUnitAggregateServerAware action = (JsUnitAggregateServerAware) targetAction;
            action.setAggregateServer(JsUnitAggregateServer.instance());
        }
    }

}
