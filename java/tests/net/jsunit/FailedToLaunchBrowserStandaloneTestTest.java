package net.jsunit;

import junit.framework.AssertionFailedError;
import net.jsunit.configuration.ConfigurationConstants;
import net.jsunit.model.ResultType;

public class FailedToLaunchBrowserStandaloneTestTest extends StandaloneTest {

	public FailedToLaunchBrowserStandaloneTestTest(String name) {
		super(name);
	}
	
	  public void setUp() throws Exception {
	      System.setProperty(ConfigurationConstants.BROWSER_FILE_NAMES, "no_such_browser.exe");
	      System.setProperty(ConfigurationConstants.URL,
	         "http://localhost:8080/jsunit/testRunner.html?"
	         + "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true&resultId=foobar");
	      super.setUp();
	  }
	  
	  public void testStandaloneRun() throws Exception {
		  try {
			  super.testStandaloneRun();
			  fail();
		  } catch (AssertionFailedError e) {
		  }
		  assertEquals(ResultType.FAILED_TO_LAUNCH, runner.lastResult().getResultType());
	  }

	  public void tearDown() throws Exception {
	      super.tearDown();
	      System.getProperties().remove(ConfigurationConstants.BROWSER_FILE_NAMES);
	      System.getProperties().remove(ConfigurationConstants.URL);
	  }

}
