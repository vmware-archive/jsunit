package net.jsunit;

import java.io.File;

import junit.framework.TestCase;

public class BrowserResultLogWriterTest extends TestCase {

	public void testSimple() {
		assertTrue(new BrowserResultLogWriter(new File(".")).isReady());
	}
	
}
