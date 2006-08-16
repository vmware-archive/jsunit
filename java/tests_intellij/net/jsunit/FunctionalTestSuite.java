package net.jsunit;

import junit.framework.TestSuite;

public class FunctionalTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite("All functional tests");
        result.addTest(ServerFunctionalTestSuite.suite());
        result.addTest(AggregateServerFunctionalTestSuite.suite());
        return result;
    }

}
