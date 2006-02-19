package net.jsunit.plugin.eclipse.resultsui.node;

import net.jsunit.model.TestCaseResult;
import junit.framework.TestCase;

public class TestCaseResultNodeTest extends TestCase {

	public void testDisplayString() {
		TestCaseResult testCaseResult = TestCaseResult.fromString("file:///dummy%20path/dummyPage.html:testFoo|1.3|S||");
		TestCaseResultNode node = new TestCaseResultNode(testCaseResult);
		assertEquals("dummyPage.html:testFoo (success)", node.getDisplayString(false));
		assertEquals("file:///dummy path/dummyPage.html:testFoo (success)", node.getDisplayString(true));

		testCaseResult = TestCaseResult.fromString("c:\\dummy\\path\\dummyPage.html:testFoo|1.3|S||");
		node = new TestCaseResultNode(testCaseResult);
		assertEquals("dummyPage.html:testFoo (success)", node.getDisplayString(false));
		assertEquals("c:\\dummy\\path\\dummyPage.html:testFoo (success)", node.getDisplayString(true));
	}
	
}
