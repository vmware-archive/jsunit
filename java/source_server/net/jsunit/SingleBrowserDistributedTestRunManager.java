package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.Browser;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class SingleBrowserDistributedTestRunManager extends DistributedTestRunManager {
    private URL remoteMachineURL;
    private Browser remoteBrowser;

    protected SingleBrowserDistributedTestRunManager(
            RemoteServerHitter serverHitter, Configuration localConfiguration, URL remoteMachineURL, String overrideURL, Browser remoteBrowser) {
        super(serverHitter, localConfiguration, overrideURL);
        this.remoteMachineURL = remoteMachineURL;
        this.remoteBrowser = remoteBrowser;
    }

    protected void appendExtraParametersToURL(StringBuffer buffer, boolean hasExistingParameter) {
        super.appendExtraParametersToURL(buffer, hasExistingParameter);
        if (remoteBrowser != null) {
            buffer.append(hasExistingParameter ? "&" : "?");
            buffer.append("browserId=").append(remoteBrowser.getId());
        }
    }

    protected List<URL> remoteMachineURLs() {
        return Arrays.asList(new URL[]{remoteMachineURL});
    }

}
