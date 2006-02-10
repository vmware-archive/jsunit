package net.jsunit;

import junit.framework.AssertionFailedError;
import net.jsunit.configuration.ConfigurationConstants;
import net.jsunit.model.ResultType;

public class ExternallyShutDownStandaloneTestTest extends StandaloneTest {

	public ExternallyShutDownStandaloneTestTest(String name) {
		super(name);
	}
	
	  public void setUp() throws Exception {
	      System.setProperty(ConfigurationConstants.BROWSER_FILE_NAMES, JsUnitServer.DEFAULT_SYSTEM_BROWSER);
	      System.setProperty(ConfigurationConstants.URL,
	         "http://localhost:8080/jsunit/testRunner.html?"
	         + "testPage=http://localhost:8080/jsunit/tests/jsUnitTestSuite.html&autoRun=true&submitresults=true&resultId=foobar");
	      super.setUp();
	  }
	  
	  public void testStandaloneRun() throws Exception {
		  new Thread() {
			  public void run() {
				try {
					Thread.sleep(4);
				} catch (InterruptedException e) {
				}
				JsUnitServer.instance().getBrowserProcess().destroy();
			  }
		  }.start();
		  try {
			  super.testStandaloneRun();
			  fail();
		  } catch (AssertionFailedError e) {
		  }
		  assertEquals(ResultType.EXTERNALLY_SHUT_DOWN, runner.lastResult().getResultType());
	  }

	  public void tearDown() throws Exception {
	      super.tearDown();
	      System.getProperties().remove(ConfigurationConstants.BROWSER_FILE_NAMES);
	      System.getProperties().remove(ConfigurationConstants.URL);
	  }
	
}
