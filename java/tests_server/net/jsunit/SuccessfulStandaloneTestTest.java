package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import junit.framework.TestCase;
import junit.framework.TestResult;

public class SuccessfulStandaloneTestTest extends TestCase {

    public SuccessfulStandaloneTestTest(String name) {
        super(name);
    }

    protected ConfigurationSource configurationSource() {
        return new StubConfigurationSource() {
            public String browserFileNames() {
                return BrowserLaunchSpecification.DEFAULT_SYSTEM_BROWSER + "," + BrowserLaunchSpecification.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://localhost:8080/jsunit/testRunner.html?" +
                        "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html" +
                        "&autoRun=true&submitresults=true&resultId=foobar";
            }
        };
    }

    public void testSuccessfulRun() throws Exception {
        StandaloneTest test = new StandaloneTest(configurationSource());
        TestResult result = test.run();
        assertTrue(result.wasSuccessful());
        assertTrue(test.getServer().lastResult().wasSuccessful());
    }

}
