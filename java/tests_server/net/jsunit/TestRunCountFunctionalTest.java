package net.jsunit;

public class TestRunCountFunctionalTest extends FunctionalTestCase {

    public void testSimple() throws Exception {
        server.finishTestRun();
        server.finishTestRun();
        server.finishTestRun();
        webTester.beginAt("testruncount");
        String responseText = webTester.getDialog().getResponseText();
        assertEquals("3", responseText);
    }

}
