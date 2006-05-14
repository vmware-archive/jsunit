package net.jsunit;

import junit.framework.TestSuite;
import net.jsunit.model.BrowserResultBuilderTest;
import net.jsunit.model.DistributedTestRunResultBuilderTest;

public class FunctionalTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AcceptorFunctionalTest.class);
        result.addTestSuite(RunnerServiceAggregateServerFunctionalTest.class);
        result.addTestSuite(BrowserResultBuilderTest.class);
        result.addTestSuite(ClientFunctionalTest.class);
        result.addTestSuite(DisplayerFunctionalTest.class);
        result.addTestSuite(DistributedTestRunResultBuilderTest.class);
        result.addTestSuite(InvalidRemoteMachinesDistributedTestTest.class);
        result.addTestSuite(FailedToLaunchBrowserStandaloneTestTest.class);
        result.addTestSuite(FailingDistributedTestTest.class);
        result.addTestSuite(FragmentRunnerPageAggregateServerFunctionalTest.class);
        result.addTestSuite(FragmentRunnerPageFunctionalTest.class);
        result.addTestSuite(HelpPageFunctionalTest.class);
        result.addTestSuite(OverrideURLDistributedTestTest.class);
        result.addTestSuite(RemoteConfigurationSourceFunctionalTest.class);
        result.addTestSuite(RunnerFunctionalTest.class);
        result.addTestSuite(CaptchaFunctionalTest.class);
        result.addTestSuite(ServerAdminPageFunctionalTest.class);
        result.addTestSuite(ServerDefaultPageFunctionalTest.class);
        result.addTestSuite(ServerStatusFunctionalTest.class);
        result.addTestSuite(SerialDistributedTestTest.class);
        result.addTestSuite(CreateAccountFunctionalTest.class);
        result.addTestSuite(SpecificBrowserDistributedTestTest.class);
        result.addTestSuite(SuccessfulStandaloneTestTest.class);
        result.addTestSuite(TestRunServiceClientTest.class);
        result.addTestSuite(TestRunCountFunctionalTest.class);
        result.addTestSuite(TimedOutBrowserStandaloneTestTest.class);
        result.addTestSuite(TwoValidLocalhostsDistributedTestTest.class);
        result.addTestSuite(UploadRunnerPageFunctionalTest.class);
        result.addTestSuite(UrlRunnerPageFunctionalTest.class);
        result.addTestSuite(UrlOverrideStandaloneTestTest.class);
        return result;
    }
}
