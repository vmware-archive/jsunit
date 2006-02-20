package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class SuccessfulStandaloneTestTest extends StandaloneTest {

  public SuccessfulStandaloneTestTest(String name) {
      super(name);
  }

	protected ConfigurationSource configurationSource() {
		return new StubConfigurationSource() {
			public String browserFileNames() {
				return JsUnitServer.DEFAULT_SYSTEM_BROWSER + "," + JsUnitServer.DEFAULT_SYSTEM_BROWSER;
			}
			
			public String url() {
		         return "http://localhost:8080/jsunit/testRunner.html?" +
		         		"testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html" +
		         		"&autoRun=true&submitresults=true&resultId=foobar";				
			}
		};
	}
  
  public void testStandaloneRun() throws Exception {
	  super.testStandaloneRun();
	  assertTrue(server.lastResult().wasSuccessful());
  }

}
