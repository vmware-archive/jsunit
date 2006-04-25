package net.jsunit.interceptor;

import com.opensymphony.xwork.interceptor.Interceptor;
import com.opensymphony.xwork.ActionInvocation;
import net.jsunit.action.RequestSourceAware;

public class LocalhostOnlyInterceptor implements Interceptor {
    public static final String DENIED_NOT_LOCALHOST = "deniedNotLocalhost";

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        RequestSourceAware aware = (RequestSourceAware) invocation.getAction();
        String ipAddress = aware.getRequestIpAddress();
        if (ipAddress==null || ipAddress.equals("127.0.0.1"))
            return invocation.invoke();
        else
            return DENIED_NOT_LOCALHOST;
    }
}
