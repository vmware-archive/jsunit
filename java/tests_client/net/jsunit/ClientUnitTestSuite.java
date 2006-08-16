package net.jsunit;

import junit.framework.TestSuite;
import net.jsunit.client.TestRunServiceClientTest;
import net.jsunit.model.TestPageTest;
import net.jsunit.model.TestSuitePageTest;

public class ClientUnitTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite("Client unit tests");
        result.addTestSuite(TestRunServiceClientTest.class);
        result.addTestSuite(TestPageTest.class);
        result.addTestSuite(TestSuitePageTest.class);
        return result;
    }

}
