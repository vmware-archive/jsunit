package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

import javax.servlet.http.HttpServletRequest;

public abstract class JsUnitInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        execute((Action) invocation.getAction());
        return invocation.invoke();
    }

    protected abstract void execute(Action targetAction) throws Exception;

    protected HttpServletRequest request() {
        return ServletActionContext.getRequest();
    }

}
