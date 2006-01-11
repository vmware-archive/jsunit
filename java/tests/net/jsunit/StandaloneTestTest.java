package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class StandaloneTestTest extends StandaloneTest {

  public StandaloneTestTest(String name) {
      super(name);
  }

  public void setUp() throws Exception {
      System.setProperty(ConfigurationSource.BROWSER_FILE_NAMES, JsUnitServer.DEFAULT_SYSTEM_BROWSER + "," + JsUnitServer.DEFAULT_SYSTEM_BROWSER);
      System.setProperty(ConfigurationSource.URL,
         "http://localhost:8080/jsunit/testRunner.html?"
         + "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true&resultId=foobar");
      super.setUp();
  }

  public void tearDown() throws Exception {
      super.tearDown();
      System.getProperties().remove(ConfigurationSource.BROWSER_FILE_NAMES);
      System.getProperties().remove(ConfigurationSource.URL);
  }

}