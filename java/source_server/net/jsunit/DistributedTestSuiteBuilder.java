package net.jsunit;

import junit.framework.TestSuite;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.model.Browser;

import java.net.URL;
import java.util.List;

public class DistributedTestSuiteBuilder {
    private ConfigurationSource originalSource;

    public DistributedTestSuiteBuilder(ConfigurationSource originalSource) {
        this.originalSource = originalSource;
    }

    public void addTestsTo(TestSuite suite) {
        Configuration configuration = new Configuration(originalSource);
        for (final URL remoteMachineURL : configuration.getRemoteMachineURLs()) {
            RemoteConfigurationSource remoteSource = new RemoteConfigurationSource(new RemoteMachineRunnerHitter(), remoteMachineURL.toString());
            Configuration remoteMachineConfiguration = new Configuration(remoteSource);
            addTestsForRemoteConfigurationTo(remoteMachineConfiguration, remoteMachineURL, suite);
        }
    }

    private void addTestsForRemoteConfigurationTo(Configuration remoteMachineConfiguration, URL remoteMachineURL, TestSuite suite) {
        List<Browser> browsers = remoteMachineConfiguration.getBrowsers();
        if (browsers.isEmpty()) {
            DistributedTest distributedTest = createDistributedTest(originalSource, remoteMachineURL);
            suite.addTest(distributedTest);
        } else {
            for (Browser browser : browsers) {
                DistributedTest distributedTest = createDistributedTest(originalSource, remoteMachineURL);
                distributedTest.limitToBrowser(browser);
                suite.addTest(distributedTest);
            }
        }
    }

    private DistributedTest createDistributedTest(ConfigurationSource originalSource, final URL remoteMachineURL) {
        return new DistributedTest(
                originalSource,
                new DelegatingConfigurationSource(originalSource) {
                    public String remoteMachineURLs() {
                        return remoteMachineURL.toString();
                    }
                }
        );
    }


}
