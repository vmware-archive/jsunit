package net.jsunit.test;
import net.jsunit.*;

public class JsUnitTestCaseResultTest extends JsUnitTest {
	public JsUnitTestCaseResultTest(String name) {
		super(name);
	}
	public void testBuildSuccessfulResultFromString() {
		JsUnitTestCaseResult result = JsUnitTestCaseResult.fromString("testFoo|1.3|S||");
		assertEquals("testFoo", result.getName());
		assertEquals(1.3d, result.getTime(), .001d);
		assertFalse(result.hadError());
		assertFalse(result.hadFailure());
		assertTrue(result.hadSuccess());
		assertNull(result.getError());				
		assertNull(result.getFailure());
		//
		assertEquals("<testcase name=\"testFoo\" time=\"1.3\" />", result.writeXmlFragment());		
	}
	public void testBuildErrorResultFromString() {
		JsUnitTestCaseResult result = JsUnitTestCaseResult.fromString("testFoo|1.3|E|Error Message|");
		assertEquals("testFoo", result.getName());
		assertEquals(1.3d, result.getTime(), .001d);
		assertTrue(result.hadError());
		assertFalse(result.hadFailure());
		assertFalse(result.hadSuccess());
		assertEquals("Error Message", result.getError());
		assertNull(result.getFailure());
		//
		assertEquals("<testcase name=\"testFoo\" time=\"1.3\"><error message=\"Error Message\" /></testcase>", result.writeXmlFragment());
	}
	public void testBuildFailureResultFromString() {
		JsUnitTestCaseResult result = JsUnitTestCaseResult.fromString("testFoo|1.3|F|Failure Message|");
		assertEquals("testFoo", result.getName());
		assertEquals(1.3d, result.getTime(), .001d);
		assertFalse(result.hadError());
		assertTrue(result.hadFailure());
		assertFalse(result.hadSuccess());
		assertNull(result.getError());
		assertEquals("Failure Message", result.getFailure());
		//
		assertEquals("<testcase name=\"testFoo\" time=\"1.3\"><failure message=\"Failure Message\" /></testcase>", result.writeXmlFragment());
	}
}
