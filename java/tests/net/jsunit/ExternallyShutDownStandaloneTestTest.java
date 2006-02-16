package net.jsunit;

import junit.framework.AssertionFailedError;
import net.jsunit.configuration.ConfigurationProperty;
import net.jsunit.model.ResultType;

public class ExternallyShutDownStandaloneTestTest extends StandaloneTest {

	public ExternallyShutDownStandaloneTestTest(String name) {
		super(name);
	}
	
	  public void setUp() throws Exception {
	      System.setProperty(ConfigurationProperty.BROWSER_FILE_NAMES.getName(), JsUnitServer.DEFAULT_SYSTEM_BROWSER);
	      System.setProperty(ConfigurationProperty.URL.getName(),
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
				JsUnitServer.serverInstance().getBrowserProcess().destroy();
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
	      System.getProperties().remove(ConfigurationProperty.BROWSER_FILE_NAMES.getName());
	      System.getProperties().remove(ConfigurationProperty.URL.getName());
	  }
	
}
