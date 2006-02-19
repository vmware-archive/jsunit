package net.jsunit.plugin.eclipse.resultsui.node;

public class BrowserResultNodeTest extends TestCaseResultNodeTest {

	public void testDisplayString() {
		BrowserResultNode node = new BrowserResultNode("c:\\program files\\firefox.exe");
		assertEquals("c:\\program files\\firefox.exe", node.getDisplayString(true));
		assertEquals("firefox.exe", node.getDisplayString(false));

		node = new BrowserResultNode("/usr/local/firefox.exe");
		assertEquals("/usr/local/firefox.exe", node.getDisplayString(true));
		assertEquals("firefox.exe", node.getDisplayString(false));
	}
	
}
