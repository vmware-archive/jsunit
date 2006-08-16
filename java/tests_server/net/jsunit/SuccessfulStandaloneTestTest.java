package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.StubConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.utility.FileUtility;

public class SuccessfulStandaloneTestTest extends EndToEndTestCase {

    protected ConfigurationSource configurationSource() {
        return new StubConfigurationSource() {
            public String browserFileNames() {
                return Browser.DEFAULT_SYSTEM_BROWSER + "," + Browser.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://localhost:" + port + "/jsunit/testRunner.html?" +
                        "testPage=http://localhost:" + port + "/jsunit/tests/jsUnitTestSuite.html" +
                        "&autoRun=true&submitresults=true&resultId=foobar";
            }

            public String port() {
                return String.valueOf(port);
            }

            public String resourceBase() {
                return FileUtility.jsUnitPath().getAbsolutePath();
            }

        };
    }

    public void testSuccessfulRun() throws Exception {
        assertSuccessful(new StandaloneTest(configurationSource()));
    }

}
