package net.jsunit;

import net.jsunit.model.ResultType;

public class UrlRunnerPageFunctionalTest extends FunctionalTestCase {

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/urlRunnerPage?showSkinOptions");
    }

    public void testInitialConditions() throws Exception {
        assertOnUrlRunnerPage();
    }

    public void testRunnerForParticularBrowser() throws Exception {
        setUpURLRunnerSubmission();
        webTester.setFormElement("browserId", "0");
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                "http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html",
                1
        );
    }

    private void setUpURLRunnerSubmission() {
        webTester.setFormElement("url", "http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
        webTester.selectOption("skinId", "None (raw XML)");
    }

    public void testRunnerForAllBrowsers() throws Exception {
        setUpURLRunnerSubmission();
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                "http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html",
                2
        );
    }

    public void testRunnerWithHTMLSkin() throws Exception {
        setUpURLRunnerSubmission();
        webTester.setFormElement("browserId", "");
        webTester.selectOption("skinId", "HTML");
        webTester.submit();
        webTester.assertTitleEquals("JsUnit Test Results");
        webTester.assertTextPresent("http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
//        webTester.assertTextPresent(ResultType.SUCCESS.getDisplayString());
    }

    public void testRunnerWithTextSkin() throws Exception {
        setUpURLRunnerSubmission();
        webTester.setFormElement("browserId", "");
        webTester.selectOption("skinId", "Text");
        webTester.submit();
//        String responseText = webTester.getDialog().getResponseText();
//        assertTrue(responseText.indexOf("http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:") != -1);
//        assertTrue(responseText.indexOf(ResultType.SUCCESS.getDisplayString()) != -1);
    }

}
