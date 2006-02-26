package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FunctionalTestSuite extends TestCase {
    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AcceptorFunctionalTest.class);
        result.addTestSuite(DisplayerFunctionalTest.class);
        result.addTestSuite(InvalidRemoteMachinesDistributedTestTest.class);
        result.addTestSuite(FailedToLaunchBrowserStandaloneTestTest.class);
        result.addTestSuite(LandingPageFunctionalTest.class);
//        result.addTestSuite(FarmServerFunctionalTest.class);
        result.addTestSuite(OverrideURLDistributedTestTest.class);
        result.addTestSuite(RunnerFunctionalTest.class);
        result.addTestSuite(ServerConfigurationFunctionalTest.class);
        result.addTestSuite(SuccessfulStandaloneTestTest.class);
        result.addTestSuite(TimedOutBrowserStandaloneTestTest.class);
        result.addTestSuite(TwoValidLocalhostsDistributedTestTest.class);
//        result.addTestSuite(ExternallyShutDownStandaloneTestTest.class);
        result.addTestSuite(UrlOverrideStandaloneTestTest.class);
        
        return result;
    }
}
