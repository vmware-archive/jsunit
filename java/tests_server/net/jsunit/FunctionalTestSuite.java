package net.jsunit;

import junit.framework.TestSuite;
import net.jsunit.model.BrowserResultBuilderTest;
import net.jsunit.model.DistributedTestRunResultBuilderTest;

public class FunctionalTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AcceptorFunctionalTest.class);
        result.addTestSuite(BrowserResultBuilderTest.class);
        result.addTestSuite(ConfigurationFunctionalTest.class);
        result.addTestSuite(DisplayerFunctionalTest.class);
        result.addTestSuite(DistributedTestRunResultBuilderTest.class);
        result.addTestSuite(InvalidRemoteMachinesDistributedTestTest.class);
        result.addTestSuite(FailedToLaunchBrowserStandaloneTestTest.class);
//        result.addTestSuite(FailingDistributedTestTest.class);
        result.addTestSuite(ServerLandingPageFunctionalTest.class);
        result.addTestSuite(OverrideURLDistributedTestTest.class);
        result.addTestSuite(RemoteConfigurationSourceFunctionalTest.class);
        result.addTestSuite(RunnerFunctionalTest.class);
        result.addTestSuite(ServerStatusFunctionalTest.class);
        result.addTestSuite(SerialDistributedTestTest.class);
        result.addTestSuite(SpecificBrowserDistributedTestTest.class);
        result.addTestSuite(SuccessfulStandaloneTestTest.class);
        result.addTestSuite(TestRunCountFunctionalTest.class);
        result.addTestSuite(TimedOutBrowserStandaloneTestTest.class);
        result.addTestSuite(TwoValidLocalhostsDistributedTestTest.class);
        result.addTestSuite(UrlOverrideStandaloneTestTest.class);
        return result;
    }
}
