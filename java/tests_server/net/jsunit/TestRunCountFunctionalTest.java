package net.jsunit;

public class TestRunCountFunctionalTest extends StandardServerFunctionalTestCase {

    public void testSimple() throws Exception {
        long initialCount = server.getTestRunCount();
        server.finishTestRun();
        server.finishTestRun();
        server.finishTestRun();
        webTester.beginAt("testruncount");
        String responseText = webTester.getDialog().getResponseText();
        assertEquals(String.valueOf(initialCount + 3), responseText);
    }

}
