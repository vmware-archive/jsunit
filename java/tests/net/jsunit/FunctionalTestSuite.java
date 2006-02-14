package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class FunctionalTestSuite extends TestCase {
    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AcceptorFunctionalTest.class);
        result.addTestSuite(DisplayerFunctionalTest.class);
        result.addTestSuite(DistributedTestTest.class);
        result.addTestSuite(FailedToLaunchBrowserStandaloneTestTest.class);
//        result.addTestSuite(FarmFunctionalTest.class);
        result.addTestSuite(RunnerFunctionalTest.class);
        result.addTestSuite(ServerConfigurationFunctionalTest.class);
        result.addTestSuite(SuccessfulStandaloneTestTest.class);
        result.addTestSuite(TimedOutBrowserStandaloneTestTest.class);
//        result.addTestSuite(ExternallyShutDownStandaloneTestTest.class);
        result.addTestSuite(UrlOverrideStandaloneTestTest.class);
        
        return result;
    }
}
