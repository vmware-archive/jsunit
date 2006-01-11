package net.jsunit.interceptor;

import net.jsunit.action.BrowserResultAware;
import net.jsunit.model.BrowserResult;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;

public class BrowserResultInterceptor extends InterceptorSupport {

	protected void execute(Action targetAction) {
        BrowserResult result = BrowserResult.fromRequest(ServletActionContext.getRequest());
        BrowserResultAware aware = (BrowserResultAware) targetAction;
        aware.setBrowserResult(result);
	}

}
