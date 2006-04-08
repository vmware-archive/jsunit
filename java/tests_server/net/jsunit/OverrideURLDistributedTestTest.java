package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.Browser;

public class OverrideURLDistributedTestTest extends EndToEndTestCase {

    protected ConfigurationSource aggregateSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:" + port;
            }
        };
    }

    protected ConfigurationSource serverSourceWithBadTestURL() {
        return new StubConfigurationSource() {
            public String browserFileNames() {
                return Browser.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://www.example.com";
            }

            public String port() {
                return String.valueOf(port);
            }
        };
    }

    public void testOverrideURL() throws Throwable {
        DistributedTest test = new DistributedTest(serverSourceWithBadTestURL(), aggregateSource());
        test.setOverrideURL(
                "http://localhost:" + port + "/jsunit/testRunner.html?" +
                        "testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true"
        );
        assertSuccessful(test);
    }

}
