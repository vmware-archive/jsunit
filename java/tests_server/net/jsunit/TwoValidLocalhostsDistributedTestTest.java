package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.ResultType;

public class TwoValidLocalhostsDistributedTestTest extends DistributedTest {

    public TwoValidLocalhostsDistributedTestTest(String name) {
        super(name);
    }

    protected ConfigurationSource farmConfigurationSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:8080, http://localhost:8080";
            }
        };
    }

    protected StubConfigurationSource configurationSource() {
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
        super.testCollectResults();
        assertEquals(ResultType.SUCCESS, manager.getFarmTestRunResult().getResultType());
    }

}