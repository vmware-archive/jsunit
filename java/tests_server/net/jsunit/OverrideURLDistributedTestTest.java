package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.StubConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.utility.FileUtility;

public class OverrideURLDistributedTestTest extends EndToEndTestCase {

    protected ConfigurationSource sourceWithBadTestURL() {
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

            public String resourceBase() {
                return FileUtility.jsUnitPath().getAbsolutePath();
            }

            public String remoteMachineURLs() {
                return "http://localhost:" + port;
            }

        };
    }

    public void testOverrideURL() throws Throwable {
        DistributedTest test = new DistributedTest(sourceWithBadTestURL());
        test.setOverrideURL(
                "http://localhost:" + port + "/jsunit/testRunner.html?" +
                        "testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true"
        );
        assertSuccessful(test);
    }

}
