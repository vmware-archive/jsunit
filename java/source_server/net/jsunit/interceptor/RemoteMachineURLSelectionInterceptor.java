package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import net.jsunit.InvalidUrlIdException;
import net.jsunit.action.RemoteMachineURLSelectionAware;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RemoteMachineURLSelectionInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        RemoteMachineURLSelectionAware aware = (RemoteMachineURLSelectionAware) invocation.getAction();
        String[] urlIds = ServletActionContext.getRequest().getParameterValues("urlId_browserId");
        List<URL> result = null;
        List<URL> allURLs = aware.getAllRemoteMachineURLs();
        if (urlIds != null && urlIds.length > 0) {
            try {
                result = selectedUrls(urlIds, allURLs);
            } catch (InvalidUrlIdException e) {
                aware.setInvalidRemoteMachineURLId(e.getIdString());
                return Action.ERROR;
            }
        }
        if (result == null)
            result = allURLs;
        aware.setSelectedRemoteMachineURLs(result);
        return invocation.invoke();
    }

    private List<URL> selectedUrls(String[] selectedUrlIds, List<URL> allURLs) throws InvalidUrlIdException {
        List<URL> result = new ArrayList<URL>();
        for (String idString : selectedUrlIds) {
            try {
                int id = Integer.parseInt(idString);
                result.add(allURLs.get(id));
            } catch (Exception e) {
                throw new InvalidUrlIdException(idString);
            }
        }
        return result;
    }

}
