package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.action.RequestSourceAware;

public class RequestSourceInterceptor extends JsUnitInterceptor {

    protected void execute(Action targetAction) throws Exception {
        RequestSourceAware aware = ((RequestSourceAware) targetAction);
        aware.setRequestIPAddress(request().getRemoteAddr());
        aware.setRequestHost(request().getRemoteHost());
    }

}
