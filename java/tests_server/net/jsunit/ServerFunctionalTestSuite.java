package net.jsunit;

import junit.framework.TestSuite;

public class ServerFunctionalTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite("Server functional test suite");
        result.addTestSuite(AcceptorFunctionalTest.class);
        result.addTestSuite(DisplayerFunctionalTest.class);
        result.addTestSuite(InvalidRemoteMachinesDistributedTestTest.class);
        result.addTestSuite(FailedToLaunchBrowserStandaloneTestTest.class);
        result.addTestSuite(FailingDistributedTestTest.class);
        result.addTestSuite(OverrideURLDistributedTestTest.class);
        result.addTestSuite(RemoteConfigurationSourceFunctionalTest.class);
        result.addTestSuite(RunnerFunctionalTest.class);
        result.addTestSuite(SerialDistributedTestTest.class);
        result.addTestSuite(SpecificBrowserDistributedTestTest.class);
        result.addTestSuite(SuccessfulStandaloneTestTest.class);
        result.addTestSuite(TwoValidLocalhostsDistributedTestTest.class);
        result.addTestSuite(UrlOverrideStandaloneTestTest.class);
        result.addTestSuite(TimedOutBrowserStandaloneTestTest.class);
        return result;
    }

}
