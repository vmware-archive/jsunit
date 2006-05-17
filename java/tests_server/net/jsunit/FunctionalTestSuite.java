package net.jsunit;

import junit.framework.TestSuite;

public class FunctionalTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite("All Functional Tests");
        result.addTest(StandardServerFunctionalTestSuite.suite());
        result.addTest(AggregateServerFunctionalTestSuite.suite());
        return result;
    }
}
