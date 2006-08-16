package net.jsunit;

import junit.framework.TestSuite;

public class AllTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite("All tests");
        result.addTest(UnitTestSuite.suite());
        result.addTest(FunctionalTestSuite.suite());
        return result;
    }

}
