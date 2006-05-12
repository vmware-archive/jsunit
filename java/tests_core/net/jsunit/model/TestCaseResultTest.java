package net.jsunit.model;

import junit.framework.TestCase;

public class TestCaseResultTest extends TestCase {
    public TestCaseResultTest(String name) {
        super(name);
    }

    public void testBuildSuccessfulResultFromString() {
        TestCaseResult result = TestCaseResult.fromString("file:///dummy%20path/dummyPage.html:testFoo|1.3|S||");
        assertEquals("file:///dummy path/dummyPage.html", result.getTestPageName());
        assertEquals("testFoo", result.getName());
        assertEquals("file:///dummy path/dummyPage.html:testFoo", result.getFullyQualifiedName());
        assertEquals(1.3d, result.getTime(), 0.1d);
        assertFalse(result.hadError());
        assertFalse(result.hadFailure());
        assertTrue(result.wasSuccessful());
        assertNull(result.getError());
        assertNull(result.getFailure());
        assertEquals(ResultType.SUCCESS, result._getResultType());
        assertEquals("<testCaseResult type=\"SUCCESS\" name=\"file:///dummy path/dummyPage.html:testFoo\" time=\"1.3\" />", result.getXmlFragment());
    }

    public void testProblemSummary() {
        TestCaseResult result = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|");
        assertEquals("file:///dummy/path/dummyPage.html:testFoo had an error: Test Error Message", result.getProblemSummary(true));
    }

    public void testBuildErrorResultFromString() {
        TestCaseResult result = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|");
        assertEquals("file:///dummy/path/dummyPage.html:testFoo", result.getFullyQualifiedName());
        assertEquals(1.3d, result.getTime());
        assertTrue(result.hadError());
        assertFalse(result.hadFailure());
        assertFalse(result.wasSuccessful());
        assertEquals("Test Error Message", result.getError());
        assertNull(result.getFailure());
        assertEquals(ResultType.ERROR, result._getResultType());
        assertEquals("<testCaseResult type=\"ERROR\" name=\"file:///dummy/path/dummyPage.html:testFoo\" time=\"1.3\"><error>Test Error Message</error></testCaseResult>", result.getXmlFragment());
    }

    public void testBuildFailureResultFromString() {
        TestCaseResult result = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|F|Test Failure Message|");
        assertEquals("file:///dummy/path/dummyPage.html:testFoo", result.getFullyQualifiedName());
        assertEquals(1.3d, result.getTime());
        assertFalse(result.hadError());
        assertTrue(result.hadFailure());
        assertFalse(result.wasSuccessful());
        assertNull(result.getError());
        assertEquals("Test Failure Message", result.getFailure());
        assertEquals(ResultType.FAILURE, result._getResultType());
        assertEquals("<testCaseResult type=\"FAILURE\" name=\"file:///dummy/path/dummyPage.html:testFoo\" time=\"1.3\"><failure>Test Failure Message</failure></testCaseResult>", result.getXmlFragment());
    }

    public void testFailureAsErrorString() throws Exception {
        TestCaseResult failed = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|F|Test Failure Message|");
        StringBuffer buffer = new StringBuffer();
        failed.addErrorStringTo(buffer);
        assertEquals("testFoo failed: Test Failure Message", buffer.toString());
    }

    public void testErrorAsErrorString() throws Exception {
        TestCaseResult error = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|");
        StringBuffer buffer = new StringBuffer();
        error.addErrorStringTo(buffer);
        assertEquals("testFoo had an error: Test Error Message", buffer.toString());
    }

    public void testSuccessAsErrorString() throws Exception {
        TestCaseResult success = TestCaseResult.fromString("file://dummy/path/dummyPage.html:testFoo|1.3|S||");
        StringBuffer buffer = new StringBuffer();
        success.addErrorStringTo(buffer);
        assertEquals("", buffer.toString());
    }

}
