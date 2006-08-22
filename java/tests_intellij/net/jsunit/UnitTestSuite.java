package net.jsunit;

import junit.framework.Test;
import junit.framework.TestSuite;

public class UnitTestSuite {

    public static Test suite() {
        TestSuite result = new TestSuite("All unit tests");
        result.addTest(ClientUnitTestSuite.suite());
        result.addTest(CoreUnitTestSuite.suite());
        result.addTest(ServerUnitTestSuite.suite());
        result.addTest(AggregateServerUnitTestSuite.suite());
        return result;
    }
}
