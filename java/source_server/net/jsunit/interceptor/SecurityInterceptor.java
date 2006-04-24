package net.jsunit.interceptor;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import net.jsunit.action.RequestSourceAware;

public class SecurityInterceptor implements Interceptor {
    public static final String DENIED = "denied";

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        RequestSourceAware aware = (RequestSourceAware) invocation.getAction();
        String ipAddress = aware.getRequestIpAddress();
        if (isValidIpAddress(ipAddress))
            return invocation.invoke();
        else
            return DENIED;
    }

    private boolean isValidIpAddress(String ipAddress) {
        return ipAddress == null || ipAddress.startsWith("192.168") || ipAddress.equals("127.0.0.1");
    }
}
