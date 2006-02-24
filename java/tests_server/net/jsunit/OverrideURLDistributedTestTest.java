package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.logging.NoOpStatusLogger;
import net.jsunit.model.ResultType;

public class OverrideURLDistributedTestTest extends DistributedTest {

    public OverrideURLDistributedTestTest(String name) {
        super(name);
    }

    protected ConfigurationSource farmConfigurationSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:8080";
            }

        };
    }

    protected DistributedTestRunManager createTestRunManager() {
        return new DistributedTestRunManager(
            new NoOpStatusLogger(),
            new Configuration(farmConfigurationSource()),
                "http://localhost:8080/jsunit/testRunner.html?"
                + "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true"
        );
    }

    protected StubConfigurationSource configurationSource() {
        return new StubConfigurationSource() {
            public String browserFileNames() {
                return BrowserLaunchSpecification.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://www.example.com";
            }
        };
    }

    public void testCollectResults() {
        super.testCollectResults();
        assertEquals(ResultType.SUCCESS, manager.getFarmTestRunResult().getResultType());
    }

}
