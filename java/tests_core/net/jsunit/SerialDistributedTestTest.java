// Copyright 2005 Google Inc. All Rights Reserved.

package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.TestResult;

public class SerialDistributedTestTest extends TestCase {
    public void testTwoTestsInSerial() throws Throwable {
        DummyConfigurationSource farmSource = new DummyConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:1234/";
            }
        };

        DummyConfigurationSource localServerSource = new DummyConfigurationSource() {
            public String browserFileNames() {
                return "";
            }
        };

        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new DistributedTest(localServerSource, farmSource));
        testSuite.addTest(new DistributedTest(localServerSource, farmSource));
        TestResult testResult = new TestResult();
        testSuite.run(testResult);
        assertTrue(testResult.wasSuccessful());
    }
}
