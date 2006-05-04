package net.jsunit;

import net.jsunit.configuration.Configuration;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class SingleMachineDistributedTestRunManager extends DistributedTestRunManager {
    private URL remoteMachineURL;

    public SingleMachineDistributedTestRunManager(
            RemoteServerHitter serverHitter, Configuration localConfiguration, URL remoteMachineURL, String overrideURL) {
        super(serverHitter, localConfiguration, overrideURL);
        this.remoteMachineURL = remoteMachineURL;
    }

    public List<URL> remoteMachineURLs() {
        return Arrays.asList(new URL[]{remoteMachineURL});
    }

}
