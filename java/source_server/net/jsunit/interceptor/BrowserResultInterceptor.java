package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.action.BrowserResultAware;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultWriter;
import net.jsunit.model.BrowserSource;
import net.jsunit.utility.StringUtility;

import javax.servlet.http.HttpServletRequest;

public class BrowserResultInterceptor extends JsUnitInterceptor {

    protected void execute(Action targetAction) throws Exception {
        HttpServletRequest request = request();
        BrowserResultAware aware = (BrowserResultAware) targetAction;
        BrowserResult result = build(request, aware);
        aware.setBrowserResult(result);
    }

    public BrowserResult build(HttpServletRequest request, BrowserSource source) {
        BrowserResult result = new BrowserResult();
        String testId = request.getParameter(BrowserResultWriter.ID);
        if (!StringUtility.isEmpty(testId))
            result.setId(testId);
        result.setRemoteAddress(request.getRemoteAddr());
        result.setUserAgent(request.getParameter(BrowserResultWriter.USER_AGENT));
        result.setBaseURL(request.getParameter(BrowserResultWriter.URL));
        String time = request.getParameter(BrowserResultWriter.TIME);
        if (!StringUtility.isEmpty(time))
            result.setTime(Double.parseDouble(time));
        result.setJsUnitVersion(request.getParameter(BrowserResultWriter.JSUNIT_VERSION));
        result._setTestCaseStrings(request.getParameterValues(BrowserResultWriter.TEST_CASE_RESULTS));
        String browserIdString = request.getParameter("browserId");
        if (browserIdString != null)
            result.setBrowser(source.getBrowserById(Integer.parseInt(browserIdString)));
        String userProperty = request.getParameter(BrowserResultWriter.USER_PROPERTY);
        if (userProperty != null)
            result.setUserProperty(userProperty);
        return result;
    }

}
