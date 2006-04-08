package net.jsunit;

import junit.framework.TestSuite;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.configuration.ServerType;
import net.jsunit.model.Browser;
import net.jsunit.utility.StringUtility;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DistributedTestSuiteBuilder {
    private ConfigurationSource localeSource;
    private RemoteServerHitter hitter;
    private Configuration localConfiguration;
    private int browserCount;

    public DistributedTestSuiteBuilder(ConfigurationSource localSource) {
        this(localSource, new RemoteMachineServerHitter());
    }

    public DistributedTestSuiteBuilder(ConfigurationSource localSource, RemoteServerHitter hitter) {
        this.localeSource = localSource;
        this.hitter = hitter;
        this.localConfiguration = new Configuration(localeSource);
    }

    public void addTestsTo(TestSuite suite) {
        List<RemoteConfigurationFetcher> remoteConfigurationFetchers = new ArrayList<RemoteConfigurationFetcher>();
        for (final URL remoteMachineURL : localConfiguration.getRemoteMachineURLs()) {
            RemoteConfigurationFetcher fetcher = new RemoteConfigurationFetcher(hitter, remoteMachineURL);
            fetcher.start();
            remoteConfigurationFetchers.add(fetcher);
        }
        for (RemoteConfigurationFetcher fetcher : remoteConfigurationFetchers) {
            try {
                fetcher.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Collections.sort(remoteConfigurationFetchers);
        for (RemoteConfigurationFetcher fetcher : remoteConfigurationFetchers)
            addTestsForRemoteConfigurationTo(fetcher.getRetrievedRemoteConfiguration(), fetcher.getRemoteMachineURL(), suite);

        suite.setName("JsUnit Tests (" + getRemoteMachineURLCount() + " machines, " + getBrowserCount() + " direct browsers)");
    }

    private void addTestsForRemoteConfigurationTo(Configuration remoteConfiguration, URL remoteMachineURL, TestSuite suite) {
        String remoteMachineDisplayName = StringUtility.escapeForSuiteName(remoteMachineURL.getHost()) + ":" + remoteMachineURL.getPort();
        if (!StringUtility.isEmpty(remoteConfiguration.getDescription()))
            remoteMachineDisplayName += " (" + remoteConfiguration.getDescription() + ")";
        if (remoteConfiguration.isValidFor(ServerType.AGGREGATE))
            addAggregateDistributedTestTo(remoteMachineURL, remoteMachineDisplayName, remoteConfiguration, suite);
        else
            addSuiteOfDistributedTestsTo(remoteMachineURL, remoteMachineDisplayName, remoteConfiguration, suite);
    }

    private void addSuiteOfDistributedTestsTo(URL remoteMachineURL, String remoteMachineDisplayName, Configuration remoteConfiguration, TestSuite suite) {
        List<Browser> browsers = remoteConfiguration.getBrowsers();
        TestSuite suiteForRemoteMachine = new TestSuite(remoteMachineDisplayName + " - server with " + browsers.size() + " browser(s)");
        for (Browser browser : browsers) {
            browserCount++;
            DistributedTest distributedTest = createDistributedTest(localeSource, remoteMachineURL);
            distributedTest.limitToBrowser(browser);
            suiteForRemoteMachine.addTest(distributedTest);
        }
        suite.addTest(suiteForRemoteMachine);
    }

    private void addAggregateDistributedTestTo(URL remoteMachineURL, String remoteMachineDisplayName, Configuration remoteConfiguration, TestSuite suite) {
        DistributedTest distributedTest = createDistributedTest(localeSource, remoteMachineURL);
        String name = remoteMachineDisplayName + " - aggregate server with " + remoteConfiguration.getRemoteMachineURLs().size() + " remote machine(s)";
        distributedTest.setName(name);
        suite.addTest(distributedTest);
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

    public int getRemoteMachineURLCount() {
        return localConfiguration.getRemoteMachineURLs().size();
    }

    public int getBrowserCount() {
        return browserCount;
    }

}
