package net.jsunit;

import junit.framework.AssertionFailedError;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.ResultType;

public class ExternallyShutDownStandaloneTestTest extends StandaloneTest {

	public ExternallyShutDownStandaloneTestTest(String name) {
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
		};
	}
	
	  public void testStandaloneRun() throws Exception {
		  new Thread() {
			  public void run() {
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
				}
				JsUnitServer.serverInstance().getBrowserProcess().destroy();
			  }
		  }.start();
		  try {
			  super.testStandaloneRun();
			  fail();
		  } catch (AssertionFailedError e) {
		  }
		  assertEquals(ResultType.EXTERNALLY_SHUT_DOWN, server.lastResult().getResultType());
	  }

}
