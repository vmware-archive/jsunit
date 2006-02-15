package net.jsunit;

import net.jsunit.configuration.ConfigurationProperty;

public class SuccessfulStandaloneTestTest extends StandaloneTest {

  public SuccessfulStandaloneTestTest(String name) {
      super(name);
  }

  public void setUp() throws Exception {
      System.setProperty(ConfigurationProperty.BROWSER_FILE_NAMES.getName(), JsUnitServer.DEFAULT_SYSTEM_BROWSER + "," + JsUnitServer.DEFAULT_SYSTEM_BROWSER);
      System.setProperty(ConfigurationProperty.URL.getName(),
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
      System.getProperties().remove(ConfigurationProperty.BROWSER_FILE_NAMES.getName());
      System.getProperties().remove(ConfigurationProperty.URL.getName());
  }

}