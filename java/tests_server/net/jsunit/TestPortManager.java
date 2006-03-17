package net.jsunit;

public class TestPortManager {

	private static int port = 8090;
	
	public int newPort() {
		return port++;
	}
	
}
