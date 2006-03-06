package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestResult;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.ResultType;

public class TimedOutBrowserStandaloneTestTest extends TestCase {

	public TimedOutBrowserStandaloneTestTest(String name) {
		super(name);
	}
	
	protected ConfigurationSource configurationSource() {
		return new StubConfigurationSource() {
			public String browserFileNames() {
				return BrowserLaunchSpecification.DEFAULT_SYSTEM_BROWSER;
			}
			
			public String url() {
		         return "http://localhost:8080/jsunit/testRunner.html?" +
		         		"testPage=http://localhost:8080/jsunit/tests/jsUnitTestSuite.html" +
		         		"&autoRun=true&submitresults=true&resultId=foobar";				
			}
			
			public String timeoutSeconds() {
				return "0";
			}
		};
	}

	  public void testBrowserTimesOut() throws Exception {
          StandaloneTest test = new StandaloneTest(configurationSource());
          TestResult result = test.run();
          assertFalse(result.wasSuccessful());
		  assertEquals(ResultType.TIMED_OUT, test.getServer().lastResult().getResultType());
	  }

}
