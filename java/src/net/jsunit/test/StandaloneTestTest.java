package net.jsunit.test;

import net.jsunit.Configuration;
import net.jsunit.StandaloneTest;

public class StandaloneTestTest extends StandaloneTest {

  public StandaloneTestTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    System.setProperty(Configuration.BROWSER_FILE_NAMES, DEFAULT_SYSTEM_BROWSER + "," + DEFAULT_SYSTEM_BROWSER);
    System.setProperty(Configuration.URL,
       "http://localhost:8080/jsunit/testRunner.html?"
       + "testPage=http://localhost:8080/jsunit/tests/jsUnitTestSuite.html&autoRun=true&submitresults=true&resultId=foobar");
    super.setUp();
  }

  public void tearDown() throws Exception {
    System.getProperties().remove(Configuration.BROWSER_FILE_NAMES);
    System.getProperties().remove(Configuration.URL);
    super.tearDown();
  }
}
