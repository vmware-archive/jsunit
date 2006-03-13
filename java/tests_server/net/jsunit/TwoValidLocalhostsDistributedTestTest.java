package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestResult;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.ResultType;

public class TwoValidLocalhostsDistributedTestTest extends TestCase {

    protected ConfigurationSource farmSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:8080, http://localhost:8080";
            }
        };
    }

    protected StubConfigurationSource serverSource() {
        return new StubConfigurationSource() {

            public String browserFileNames() {
                return BrowserLaunchSpecification.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://localhost:8080/jsunit/testRunner.html?"
                        + "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true";
            }
        };
    }

    public void testSuccessfulRun() {
        DistributedTest test = new DistributedTest(serverSource(), farmSource());
        TestResult testResult = test.run();
        assertTrue(testResult.wasSuccessful());
        DistributedTestRunResult distributedTestRunResult = test.getDistributedTestRunManager().getDistributedTestRunResult();
        assertEquals(ResultType.SUCCESS, distributedTestRunResult.getResultType());
        assertEquals(2, distributedTestRunResult.getTestRunResults().size());

        assertTrue(test.getTemporaryStandardServer().isTemporary());
    }

}