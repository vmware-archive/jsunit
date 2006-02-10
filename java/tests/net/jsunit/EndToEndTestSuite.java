package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class EndToEndTestSuite extends TestCase {
    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(SuccessfulStandaloneTestTest.class);
        result.addTestSuite(FailedToLaunchBrowserStandaloneTestTest.class);
        result.addTestSuite(TimedOutBrowserStandaloneTestTest.class);
//        result.addTestSuite(ExternallyShutDownStandaloneTestTest.class);
        result.addTestSuite(DistributedTestTest.class);
        return result;
    }
}
