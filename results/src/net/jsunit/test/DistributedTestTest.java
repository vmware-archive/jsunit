package net.jsunit.test;

import net.jsunit.DistributedTest;
import net.jsunit.JsUnitServer;

public class DistributedTestTest extends DistributedTest {
	public DistributedTestTest(String name) {
		super(name);
	}
	public void setUp() throws Exception {
		super.setUp();
		JsUnitServer.start();
	}
	public void tearDown() throws Exception {
		JsUnitServer.stop();
		super.tearDown();
	}
}
