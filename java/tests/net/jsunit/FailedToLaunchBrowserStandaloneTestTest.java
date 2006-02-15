package net.jsunit;

import junit.framework.AssertionFailedError;
import net.jsunit.configuration.ConfigurationProperty;
import net.jsunit.model.ResultType;

public class FailedToLaunchBrowserStandaloneTestTest extends StandaloneTest {

	public FailedToLaunchBrowserStandaloneTestTest(String name) {
		super(name);
	}
	
	  public void setUp() throws Exception {
	      System.setProperty(ConfigurationProperty.BROWSER_FILE_NAMES.getName(), "no_such_browser.exe");
	      System.setProperty(ConfigurationProperty.URL.getName(),
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
	      System.getProperties().remove(ConfigurationProperty.BROWSER_FILE_NAMES.getName());
	      System.getProperties().remove(ConfigurationProperty.URL.getName());
	  }

}
