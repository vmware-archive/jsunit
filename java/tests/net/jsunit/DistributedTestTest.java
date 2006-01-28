package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.textui.TestRunner;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationConstants;

public class DistributedTestTest extends TestCase {
    private JsUnitServer server;

    public DistributedTestTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        System.setProperty(ConfigurationConstants.BROWSER_FILE_NAMES, JsUnitServer.DEFAULT_SYSTEM_BROWSER);
        System.setProperty(ConfigurationConstants.URL,
           "http://localhost:8080/jsunit/testRunner.html?"
           + "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true");
        server = new JsUnitServer(Configuration.resolve());
        server.start();
    }

    public void tearDown() throws Exception {
        server.dispose();
        System.getProperties().remove(ConfigurationConstants.BROWSER_FILE_NAMES);
        System.getProperties().remove(ConfigurationConstants.URL);
        super.tearDown();
    }

    public void testDistributedRunWithTwoLocalhosts() {
        System.setProperty(DistributedTest.REMOTE_MACHINE_URLS, "http://localhost:8080, http://localhost:8080");
        TestResult result = TestRunner.run(new DistributedTest("testCollectResults"));
        assertEquals(1, result.runCount());
        assertTrue(result.wasSuccessful());
    }

    public void testDistributedRunWithInvalidHosts() {
        System.setProperty(DistributedTest.REMOTE_MACHINE_URLS, "http://invalid_host1:1234, http://invalid_host2:5678");
        TestResult result = TestRunner.run(new DistributedTest("testCollectResults"));
        assertEquals(1, result.runCount());
        assertFalse(result.wasSuccessful());
    }

}