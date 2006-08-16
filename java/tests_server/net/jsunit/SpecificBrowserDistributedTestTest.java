package net.jsunit;

import net.jsunit.configuration.StubConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.utility.FileUtility;

public class SpecificBrowserDistributedTestTest extends EndToEndTestCase {

    protected StubConfigurationSource source() {
        return new StubConfigurationSource() {

            public String browserFileNames() {
                return "aaaaa" + Browser.DEFAULT_SYSTEM_BROWSER + "," +
                        Browser.DEFAULT_SYSTEM_BROWSER + "," +
                        "zzzzz" + Browser.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://localhost:" + port + "/jsunit/testRunner.html?"
                        + "testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true";
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

    public void testSuccessfulRun() {
        DistributedTest test = new DistributedTest(source());
        test.limitToBrowser(new Browser(Browser.DEFAULT_SYSTEM_BROWSER, 1));
        assertSuccessful(test);
        assertEquals(1, test.getTestRunResults().size());
        assertNull(test.getTemporaryServer());
    }

}
