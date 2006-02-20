package net.jsunit;

import junit.framework.AssertionFailedError;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.ResultType;

public class TimedOutBrowserStandaloneTestTest extends StandaloneTest {

	public TimedOutBrowserStandaloneTestTest(String name) {
		super(name);
	}
	
	protected ConfigurationSource configurationSource() {
		return new StubConfigurationSource() {
			public String browserFileNames() {
				return JsUnitServer.DEFAULT_SYSTEM_BROWSER;
			}
			
			public String url() {
		         return "http://localhost:8080/jsunit/testRunner.html?" +
		         		"testPage=http://localhost:8080/jsunit/tests/jsUnitTestSuite.html" +
		         		"&autoRun=true&submitresults=true&resultId=foobar";				
			}
			
			public String timeoutSeconds() {
				return "1";
			}
		};
	}

	  public void testStandaloneRun() throws Exception {
		  try {
			  super.testStandaloneRun();
			  fail();
		  } catch (AssertionFailedError e) {
		  }
		  assertEquals(ResultType.TIMED_OUT, server.lastResult().getResultType());
	  }

}
