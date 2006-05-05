package net.jsunit;

public class TestRunCountFunctionalTest extends StandardServerFunctionalTestCase {

    public void testSimple() throws Exception {
        standardServer().finishTestRun();
        standardServer().finishTestRun();
        standardServer().finishTestRun();
        webTester.beginAt("testruncount");
        String responseText = webTester.getDialog().getResponseText();
        assertEquals("3", responseText);
    }

}
