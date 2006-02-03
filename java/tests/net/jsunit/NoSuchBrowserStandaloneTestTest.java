package net.jsunit;

import net.jsunit.configuration.ConfigurationConstants;
import net.jsunit.model.ResultType;

public class NoSuchBrowserStandaloneTestTest extends StandaloneTest {

	public NoSuchBrowserStandaloneTestTest(String name) {
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
		  super.testStandaloneRun();
		  assertEquals(ResultType.FAILED_TO_LAUNCH, runner.lastResult().getResultType());
	  }

	  public void tearDown() throws Exception {
	      super.tearDown();
	      System.getProperties().remove(ConfigurationConstants.BROWSER_FILE_NAMES);
	      System.getProperties().remove(ConfigurationConstants.URL);
	  }

}
