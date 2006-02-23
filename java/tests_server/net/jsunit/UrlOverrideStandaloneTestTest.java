package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class UrlOverrideStandaloneTestTest extends StandaloneTest {

	  public UrlOverrideStandaloneTestTest(String name) {
	      super(name);
	  }

		protected ConfigurationSource configurationSource() {
			return new StubConfigurationSource() {
				public String browserFileNames() {
					return JsUnitServer.DEFAULT_SYSTEM_BROWSER;
				}
				
				public String url() {
			         return "http://www.example.com";				
				}
				
			};
		}

	  
	  public void testStandaloneRun() throws Exception {
		  super.testStandaloneRun();
		  assertTrue(server.lastResult().wasSuccessful());
	  }

	  protected TestRunManager createTestRunManager() {
		  return new TestRunManager(server, "http://localhost:8080/jsunit/testRunner.html?"
	         + "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true&resultId=foobar");
	  }

}
