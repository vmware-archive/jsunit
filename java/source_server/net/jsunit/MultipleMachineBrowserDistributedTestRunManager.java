package net.jsunit;

import net.jsunit.configuration.Configuration;

public class MultipleMachineBrowserDistributedTestRunManager extends DistributedTestRunManager {
    protected MultipleMachineBrowserDistributedTestRunManager(RemoteServerHitter hitter, Configuration localConfiguration, String overrideURL) {
        super(hitter, localConfiguration, overrideURL);
    }
}
