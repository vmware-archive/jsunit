package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import net.jsunit.Utility;
import net.jsunit.action.BrowserResultAware;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultWriter;

import javax.servlet.http.HttpServletRequest;

public class BrowserResultInterceptor extends InterceptorSupport {

	protected void execute(Action targetAction) {
        BrowserResult result = resultFromRequest(ServletActionContext.getRequest());
        BrowserResultAware aware = (BrowserResultAware) targetAction;
        aware.setBrowserResult(result);
	}
	
    public static BrowserResult resultFromRequest(HttpServletRequest request) {
        BrowserResult result = new BrowserResult();
        String testId = request.getParameter(BrowserResultWriter.ID);
        if (!Utility.isEmpty(testId))
            result.setId(testId);
        result.setRemoteAddress(request.getRemoteAddr());
        result.setUserAgent(request.getParameter(BrowserResultWriter.USER_AGENT));
        result.setBaseURL(request.getParameter(BrowserResultWriter.URL));
        String time = request.getParameter(BrowserResultWriter.TIME);
        if (!Utility.isEmpty(time))
            result.setTime(Double.parseDouble(time));
        result.setJsUnitVersion(request.getParameter(BrowserResultWriter.JSUNIT_VERSION));
        result.setTestCaseStrings(request.getParameterValues(BrowserResultWriter.TEST_CASES));
        return result;
    }

}
