package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.action.RequestSourceAware;

import javax.servlet.http.HttpServletRequest;

public class RequestSourceInterceptor extends JsUnitInterceptor {
    public static final String REFERER_HEADER = "Referer";

    protected void execute(Action targetAction) throws Exception {
        RequestSourceAware aware = ((RequestSourceAware) targetAction);
        HttpServletRequest request = request();
        aware.setRequestIPAddress(request.getRemoteAddr());
        aware.setRequestHost(request.getRemoteHost());
        aware.setReferrer(request.getHeader(REFERER_HEADER));
    }

}
