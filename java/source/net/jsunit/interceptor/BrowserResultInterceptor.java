package net.jsunit.interceptor;

import net.jsunit.action.BrowserResultAware;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultBuilder;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;

public class BrowserResultInterceptor extends JsUnitInterceptor {

	protected void execute(Action targetAction) {
        BrowserResult result = new BrowserResultBuilder().build(ServletActionContext.getRequest());
        BrowserResultAware aware = (BrowserResultAware) targetAction;
        aware.setBrowserResult(result);
	}
	
}
