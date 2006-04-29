package net.jsunit;

import net.jsunit.model.ResultType;

public class FragmentRunnerPageFunctionalTest extends FunctionalTestCase {

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/fragmentRunnerPage");
    }

    public void testInitialConditions() throws Exception {
        assertOnFragmentRunnerPage();
    }

    public void testPasteInFragment() throws Exception {
        webTester.setFormElement("fragment", "assertTrue(true);");
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                null,
                2
        );
    }

    public void testPasteInFailingFragmentSingleBrowser() throws Exception {
        webTester.setFormElement("fragment", "assertTrue(false);");
        webTester.setFormElement("browserId", "0");
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.FAILURE,
                null,
                1
        );
    }

}
