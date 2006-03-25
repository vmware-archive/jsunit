package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.ResultType;

public class FailedToLaunchBrowserStandaloneTestTest extends EndToEndTestCase {

    protected ConfigurationSource configurationSource() {
        return new StubConfigurationSource() {
            public String browserFileNames() {
                return "no_such_browser.exe";
            }

            public String url() {
                return "http://localhost:"+port+"/jsunit/testRunner.html?" +
                        "testPage=http://localhost:"+port+"/jsunit/tests/jsUnitUtilityTests.html" +
                        "&autoRun=true&submitresults=true&resultId=foobar";
            }
            
            public String port() {
            	return String.valueOf(port);
            }
        };
    }

    public void testFailToLaunchBrowsers() throws Exception {
        assertFailure(new StandaloneTest(configurationSource()), ResultType.FAILED_TO_LAUNCH);
    }

}
