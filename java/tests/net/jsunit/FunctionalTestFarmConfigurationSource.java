package net.jsunit;

import net.jsunit.configuration.FarmConfigurationSource;

public class FunctionalTestFarmConfigurationSource implements FarmConfigurationSource {

	private int[] ports;

	public FunctionalTestFarmConfigurationSource(int... ports) {
		this.ports = ports;
	}

	public String port() {
		return String.valueOf(FunctionalTestCase.PORT);
	}

	public String logsDirectory() {
		return ".";
	}

	public String logStatus() {
		return "true";
	}

	public String timeoutSeconds() {
		return "60";
	}

	public String remoteMachineURLs() {
		StringBuffer buffer = new StringBuffer();
		for (int port : ports) {
			buffer.append("http://localhost:");
			buffer.append(port);
		}
		return buffer.toString();
	}

}
