package net.jsunit;

import junit.framework.TestSuite;
import net.jsunit.model.BrowserResultBuilderTest;
import net.jsunit.model.DistributedTestRunResultBuilderTest;

public class StandardServerFunctionalTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite("Standard Server Functional Test Suite");
        result.addTestSuite(AcceptorFunctionalTest.class);
        result.addTestSuite(BrowserResultBuilderTest.class);
        result.addTestSuite(DisplayerFunctionalTest.class);
        result.addTestSuite(DistributedTestRunResultBuilderTest.class);
        result.addTestSuite(InvalidRemoteMachinesDistributedTestTest.class);
        result.addTestSuite(FailedToLaunchBrowserStandaloneTestTest.class);
        result.addTestSuite(FailingDistributedTestTest.class);
        result.addTestSuite(OverrideURLDistributedTestTest.class);
        result.addTestSuite(RemoteConfigurationSourceFunctionalTest.class);
        result.addTestSuite(RunnerFunctionalTest.class);
        result.addTestSuite(ServerStatusFunctionalTest.class);
        result.addTestSuite(SerialDistributedTestTest.class);
        result.addTestSuite(SpecificBrowserDistributedTestTest.class);
        result.addTestSuite(SuccessfulStandaloneTestTest.class);
        result.addTestSuite(TestRunCountFunctionalTest.class);
        result.addTestSuite(TwoValidLocalhostsDistributedTestTest.class);
        result.addTestSuite(UrlOverrideStandaloneTestTest.class);
        result.addTestSuite(TimedOutBrowserStandaloneTestTest.class);
        return result;
    }

}
