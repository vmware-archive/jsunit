package net.jsunit.plugin.eclipse.resultsui.node;

import net.jsunit.model.TestPageResult;
import junit.framework.TestCase;

public class TestPageResultNodeTest extends TestCase {

	public void testDisplayString() {
		TestPageResult testPageResult = new TestPageResult("file:///c:/my/directory/myTestPage.html");
		TestPageResultNode node = new TestPageResultNode(testPageResult);
		assertEquals("file:///c:/my/directory/myTestPage.html (success)", node.getDisplayString(true));
		assertEquals("myTestPage.html (success)", node.getDisplayString(false));

		testPageResult = new TestPageResult("c:\\my\\directory\\myTestPage.html");
		node = new TestPageResultNode(testPageResult);
		assertEquals("c:\\my\\directory\\myTestPage.html (success)", node.getDisplayString(true));
		assertEquals("myTestPage.html (success)", node.getDisplayString(false));
	}
	
}
