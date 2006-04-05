package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.Browser;

public class SingleBrowserDistributedTestRunManager extends DistributedTestRunManager {
    private Browser remoteBrowser;

    protected SingleBrowserDistributedTestRunManager(
            RemoteServerHitter hitter, Configuration localConfiguration, String overrideURL, Browser remoteBrowser) {
        super(hitter, localConfiguration, overrideURL);
        this.remoteBrowser = remoteBrowser;
    }

    protected void appendExtraParametersToURL(StringBuffer buffer, boolean hasExistingParameter) {
        super.appendExtraParametersToURL(buffer, hasExistingParameter);
        if (remoteBrowser != null) {
            buffer.append(hasExistingParameter ? "&" : "?");
            buffer.append("browserId=").append(remoteBrowser.getId());
        }
    }
}
