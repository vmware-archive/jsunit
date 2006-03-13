package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestResult;
import net.jsunit.configuration.ConfigurationSource;

public class UrlOverrideStandaloneTestTest extends TestCase {

    public UrlOverrideStandaloneTestTest(String name) {
        super(name);
    }

    protected ConfigurationSource configurationSource() {
        return new StubConfigurationSource() {
            public String browserFileNames() {
                return BrowserLaunchSpecification.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://www.example.com";
            }

        };
    }

    public void testOverridenURL() throws Exception {
        StandaloneTest test = new StandaloneTest(configurationSource());
        test.setOverrideURL(
                "http://localhost:8080/jsunit/testRunner.html?testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true&resultId=foobar");
        TestResult testResult = test.run();
        assertTrue(testResult.wasSuccessful());
        assertTrue(test.getServer().lastResult().wasSuccessful());
    }

}
