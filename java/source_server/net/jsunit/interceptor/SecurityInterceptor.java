package net.jsunit.interceptor;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import net.jsunit.action.ReferrerAware;

import java.net.URL;

public class SecurityInterceptor implements Interceptor {
    public static final String DENIED = "denied";

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        ReferrerAware aware = (ReferrerAware) invocation.getAction();
        String referrer = aware.getReferrer();
        URL restrict = aware.getConfiguration().getRunnerReferrerRestrict();
        if (referrer == null || restrict == null || referrer.startsWith(restrict.toString()))
            return invocation.invoke();
        else
            return DENIED;
    }

}
