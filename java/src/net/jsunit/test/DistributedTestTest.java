package net.jsunit.test;


import junit.framework.TestResult;
import junit.textui.TestRunner;
import net.jsunit.DistributedTest;

public class DistributedTestTest extends JsUnitTestCase {
    public DistributedTestTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        server.start();
    }

    public void tearDown() throws Exception {
        server.stop();
        super.tearDown();
    }

    public void testDistributedRunWithTwoLocalhosts() {
        System.setProperty(DistributedTest.REMOTE_MACHINE_URLS, "http://localhost:8080, http://localhost:8080");
        TestResult result = TestRunner.run(new DistributedTest("testCollectResults"));
        assertTrue(result.wasSuccessful());
    }

    public void testDistributedRunWithInvalidHosts() {
        System.setProperty(DistributedTest.REMOTE_MACHINE_URLS, "http://fooXXX:1234, http://barXXX:5678");
        TestResult result = TestRunner.run(new DistributedTest("testCollectResults"));
        assertFalse(result.wasSuccessful());
    }

}