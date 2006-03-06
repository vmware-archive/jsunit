package net.jsunit;

import junit.framework.TestResult;
import junit.framework.TestCase;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.ResultType;

public class FailedToLaunchBrowserStandaloneTestTest extends TestCase {

    public FailedToLaunchBrowserStandaloneTestTest(String name) {
        super(name);
    }
	
    protected ConfigurationSource configurationSource() {
        return new StubConfigurationSource() {
            public String browserFileNames() {
                return "no_such_browser.exe";
            }
			
            public String url() {
                return "http://localhost:8080/jsunit/testRunner.html?" +
                        "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html" +
                        "&autoRun=true&submitresults=true&resultId=foobar";
            }
        };
    }

    public void testFailToLaunchBrowsers() throws Exception {
        StandaloneTest test = new StandaloneTest(configurationSource());
        TestResult result = test.run();
        assertFalse(result.wasSuccessful());
        assertEquals(ResultType.FAILED_TO_LAUNCH, test.getServer().lastResult().getResultType());
    }

}
