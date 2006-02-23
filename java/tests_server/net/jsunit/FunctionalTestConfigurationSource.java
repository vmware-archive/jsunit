/**
 * 
 */
package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class FunctionalTestConfigurationSource implements ConfigurationSource {

	private final int port;

	public FunctionalTestConfigurationSource(int port) {
		this.port = port;
	}
	
	public String resourceBase() {
		return ".";
	}

	public String port() {
		return String.valueOf(port);
	}

	public String logsDirectory() {
		return "";
	}

	public String browserFileNames() {
		return JsUnitServer.DEFAULT_SYSTEM_BROWSER;
	}

	public String url() {
		return "http://localhost:"+port+"/jsunit/testRunner.html?testPage=http://localhost:"+port+"/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true";
	}

    public String ignoreUnresponsiveRemoteMachines() {
        return "";
    }

    public String closeBrowsersAfterTestRuns() {
        return String.valueOf(Boolean.TRUE);
    }

	public String logStatus() {
		return String.valueOf(false);
	}

	public String timeoutSeconds() {
		return "60";
	}

    public String remoteMachineURLs() {
        return "";
    }    
	
}