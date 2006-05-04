package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import net.jsunit.InvalidBrowserIdException;
import net.jsunit.action.BrowserSelectionAware;
import net.jsunit.model.Browser;

import java.util.*;

public class BrowserSelectionInterceptor implements Interceptor {

    public String intercept(ActionInvocation invocation) throws Exception {
        BrowserSelectionAware aware = (BrowserSelectionAware) invocation.getAction();
        String[] browserIds = ServletActionContext.getRequest().getParameterValues("browserId");
        List<Browser> allBrowsers = aware.getAllBrowsers();
        Set<Browser> selectedBrowsers = null;
        if (browserIds != null && browserIds.length > 0) {
            try {
                selectedBrowsers = selectedBrowsers(browserIds, allBrowsers);
            } catch (InvalidBrowserIdException e) {
                aware.setInvalidBrowserId(e.getIdString());
                return Action.ERROR;
            }
        }
        List<Browser> result;
        if (selectedBrowsers == null)
            result = allBrowsers;
        else
            result = new ArrayList<Browser>(selectedBrowsers);
        Collections.sort(result);
        aware.setSelectedBrowsers(result);
        return invocation.invoke();
    }

    private Set<Browser> selectedBrowsers(String[] selectedBrowserIds, List<Browser> allBrowsers) throws InvalidBrowserIdException {
        Set<Browser> result = new HashSet<Browser>();
        for (String idString : selectedBrowserIds) {
            Browser chosenBrowser = null;
            for (Browser browser : allBrowsers) {
                try {
                    int id = Integer.parseInt(idString);
                    if (browser.hasId(id))
                        chosenBrowser = browser;
                } catch (NumberFormatException e) {
                    throw new InvalidBrowserIdException(idString);
                }
            }
            if (chosenBrowser == null) {
                throw new InvalidBrowserIdException(idString);
            } else
                result.add(chosenBrowser);
        }
        return result;
    }

    public void destroy() {
    }

    public void init() {
    }

}
