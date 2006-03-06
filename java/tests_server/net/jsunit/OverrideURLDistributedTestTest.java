package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestResult;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.ResultType;

public class OverrideURLDistributedTestTest extends TestCase {

  protected ConfigurationSource farmSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:8080";
            }

        };
    }

    protected StubConfigurationSource serverSourceWithBadTestURL() {
        return new StubConfigurationSource() {
            public String browserFileNames() {
                return BrowserLaunchSpecification.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://www.example.com";
            }
        };
    }

    public void testOverrideURL() throws Throwable {
      DistributedTest test = new DistributedTest(serverSourceWithBadTestURL(), farmSource());
      test.getDistributedTestRunManager().setOverrideURL(
          "http://localhost:8080/jsunit/testRunner.html?" +
          "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true"
      );
      TestResult result = test.run();
      assertTrue(result.wasSuccessful());

      assertEquals(
        ResultType.SUCCESS,
        test.getDistributedTestRunManager().getDistributedTestRunResult().getResultType()
      );
    }

}
