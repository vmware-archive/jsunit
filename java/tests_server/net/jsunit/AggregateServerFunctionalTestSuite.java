package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AggregateServerFunctionalTestSuite extends TestCase {
    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AggregateServerFunctionalTest.class);
        return result;
    }
}
