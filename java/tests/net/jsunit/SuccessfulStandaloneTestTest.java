package net.jsunit;

import net.jsunit.configuration.ConfigurationConstants;

public class SuccessfulStandaloneTestTest extends StandaloneTest {

  public SuccessfulStandaloneTestTest(String name) {
      super(name);
  }

  public void setUp() throws Exception {
      System.setProperty(ConfigurationConstants.BROWSER_FILE_NAMES, JsUnitServer.DEFAULT_SYSTEM_BROWSER + "," + JsUnitServer.DEFAULT_SYSTEM_BROWSER);
      System.setProperty(ConfigurationConstants.URL,
         "http://localhost:8080/jsunit/testRunner.html?"
         + "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true&resultId=foobar");
      super.setUp();
  }
  
  public void testStandaloneRun() throws Exception {
	  super.testStandaloneRun();
	  assertTrue(runner.lastResult().wasSuccessful());
  }

  public void tearDown() throws Exception {
      super.tearDown();
      System.getProperties().remove(ConfigurationConstants.BROWSER_FILE_NAMES);
      System.getProperties().remove(ConfigurationConstants.URL);
  }

}