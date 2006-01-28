package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class DummyConfigurationSource implements ConfigurationSource {

	public static final String DUMMY_URL = "http://www.example.com/";

	public String resourceBase() {
		return ".";
	}

	public String port() {
		return "1234";
	}

	public String logsDirectory() {
		return ".";
	}

	public String browserFileNames() {
		return "dummy browser filename";
	}

	public String url() {
		return DUMMY_URL;
	}

	public String closeBrowsersAfterTestRuns() {
		return "true";
	}

	public String logStatus() {
		return "false";
	}

}