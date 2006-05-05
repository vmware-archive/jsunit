package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import net.jsunit.InvalidRemoteMachineBrowserCombinationException;
import net.jsunit.RemoteRunSpecification;
import net.jsunit.action.RemoteRunSpecificationAware;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.server.RemoteRunSpecificationBuilder;

import java.util.List;

public class RemoteRunSpecificationSelectionInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        RemoteRunSpecificationAware aware = (RemoteRunSpecificationAware) invocation.getAction();
        String[] requestedUrlBrowserCombinations = ServletActionContext.getRequest().getParameterValues("urlId_browserId");
        List<RemoteRunSpecification> result = null;
        List<RemoteConfiguration> allRemoteConfigurations = aware.getAllRemoteMachineConfigurations();
        RemoteRunSpecificationBuilder builder = new RemoteRunSpecificationBuilder();
        if (requestedUrlBrowserCombinations != null && requestedUrlBrowserCombinations.length > 0) {
            try {
                result = builder.fromIdStringPairs(requestedUrlBrowserCombinations, allRemoteConfigurations);
            } catch (InvalidRemoteMachineBrowserCombinationException e) {
                aware.setInvalidRemoteMachineUrlBrowserCombination(e.createInvalidRemoteRunSpecification());
                return Action.ERROR;
            }
        }
        if (result == null)
            result = builder.forAllBrowsersFromRemoteConfigurations(allRemoteConfigurations);
        aware.setRemoteRunSpecifications(result);
        return invocation.invoke();
    }

}
