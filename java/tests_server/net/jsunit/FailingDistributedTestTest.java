package net.jsunit;

import junit.framework.TestResult;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.TestRunResult;

public class FailingDistributedTestTest extends EndToEndTestCase {

    protected ConfigurationSource farmSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:" + port;
            }

            public String port() {
                return String.valueOf(port);
            }

        };
    }

    protected StubConfigurationSource serverSource() {
        return new StubConfigurationSource() {

            public String browserFileNames() {
                return Browser.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:" + port + "/jsunit/tools/failingTest.html";
            }

            public String port() {
                return String.valueOf(port);
            }
        };
    }

    public void testFailedRun() {
        DistributedTest test = new DistributedTest(serverSource(), farmSource());
        TestResult testResult = test.run();
        assertFalse(testResult.wasSuccessful());
        assertEquals(1, test.getTestRunResults().size());
        TestRunResult testRunResult = test.getTestRunResults().get(0);
        assertEquals(1, testRunResult.getBrowserResults().size());
        BrowserResult browserResult = testRunResult.getBrowserResults().get(0);
        assertEquals(Browser.DEFAULT_SYSTEM_BROWSER, browserResult.getBrowser().getFileName());
        assertEquals(0, browserResult.getBrowser().getId());
    }


}
