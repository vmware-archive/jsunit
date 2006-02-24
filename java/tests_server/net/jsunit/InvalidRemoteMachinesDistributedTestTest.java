package net.jsunit;

import junit.framework.AssertionFailedError;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.ResultType;

public class InvalidRemoteMachinesDistributedTestTest extends DistributedTest {

    public InvalidRemoteMachinesDistributedTestTest(String name) {
        super(name);
    }

    protected ConfigurationSource farmConfigurationSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://invalid_machine1:8080, http://invalid_machine2:8080";
            }
        };
    }

    protected ConfigurationSource configurationSource() {
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

    public void testCollectResults() {
        try {
            super.testCollectResults();
            fail();
        } catch (AssertionFailedError e) {
            assertEquals(ResultType.UNRESPONSIVE, manager.getFarmTestRunResult().getResultType());
        }
    }

}