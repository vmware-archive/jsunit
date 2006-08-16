package net.jsunit;

import junit.framework.TestSuite;

public class AggregateServerFunctionalTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite("Aggregate server functional test suite");
        result.addTestSuite(AggregateConfigurationFunctionalTest.class);
        result.addTestSuite(RunnerAggregateServerFunctionalTest.class);
        return result;
    }

}
