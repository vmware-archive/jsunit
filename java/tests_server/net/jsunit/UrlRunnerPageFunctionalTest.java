package net.jsunit;

import net.jsunit.model.ResultType;

public class UrlRunnerPageFunctionalTest extends StandardServerFunctionalTestCase {

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/urlRunnerPage");
    }

    public void testInitialConditions() throws Exception {
        assertOnUrlRunnerPage();
        webTester.assertFormElementEquals("url", "");
    }

    public void testRunnerForParticularBrowser() throws Exception {
        setUpURLRunnerSubmission();
        webTester.checkCheckbox("browserId", "0");
        webTester.uncheckCheckbox("browserId", "1");
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                "http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html",
                1
        );
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
        webTester.checkCheckbox("browserId", "0");
        webTester.selectOption("skinId", "HTML");
        webTester.submit();
        webTester.gotoFrame("resultsFrame");
        webTester.assertTitleEquals("JsUnit Test Results");
        webTester.assertTextPresent("http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
        webTester.assertTextPresent(ResultType.SUCCESS.name());
    }

    public void testRunnerWithTextSkin() throws Exception {
        setUpURLRunnerSubmission();
        webTester.checkCheckbox("browserId", "1");
        webTester.selectOption("skinId", "Text");
        webTester.submit();
        webTester.gotoFrame("resultsFrame");
//        webTester.assertTitleEquals("JsUnit Test Results");
//        webTester.assertTextPresent("http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
//        webTester.assertTextPresent(ResultType.SUCCESS.name());
    }

    public void testPassingInUrl() throws Exception {
        webTester.beginAt("/urlRunnerPage?url=foobar");
        webTester.assertFormElementEquals("url", "foobar");
    }

    private void setUpURLRunnerSubmission() {
        webTester.setFormElement("url", "http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
        webTester.selectOption("skinId", "None (raw XML)");
    }

}
