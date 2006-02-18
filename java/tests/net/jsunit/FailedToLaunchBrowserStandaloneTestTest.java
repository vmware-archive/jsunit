package net.jsunit;

import junit.framework.AssertionFailedError;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.ResultType;

public class FailedToLaunchBrowserStandaloneTestTest extends StandaloneTest {

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

	
	  public void testStandaloneRun() throws Exception {
		  try {
			  super.testStandaloneRun();
			  fail();
		  } catch (AssertionFailedError e) {
		  }
		  assertEquals(ResultType.FAILED_TO_LAUNCH, runner.lastResult().getResultType());
	  }

}
