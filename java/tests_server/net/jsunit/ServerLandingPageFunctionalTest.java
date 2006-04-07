package net.jsunit;

import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.ResultType;
import net.jsunit.utility.SystemUtility;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

public class ServerLandingPageFunctionalTest extends FunctionalTestCase {

    protected boolean needsRealResultRepository() {
        return true;
    }

    public void testSlash() throws Exception {
        webTester.beginAt("/");
        assertOnLandingPage();
        webTester.assertTextPresent(SystemUtility.osString());
        webTester.assertTextPresent(SystemUtility.hostname());
        webTester.assertTextPresent(SystemUtility.ipAddress());
        webTester.assertLinkPresentWithText(new FunctionalTestConfigurationSource(port).url());
    }

    public void testIndexDotJsp() throws Exception {
        webTester.beginAt("/index.jsp");
        assertOnLandingPage();
    }

    public void testConfigLink() throws Exception {
        webTester.beginAt("/");
        webTester.clickLink("config");
        assertConfigXml();
    }

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    public void testRunnerForParticularBrowser() throws Exception {
        setUpRunnerSubmission();
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                "http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html",
                1
        );
    }

    private void setUpRunnerSubmission() {
        webTester.beginAt("/");
        webTester.setWorkingForm("urlRunnerForm");
        webTester.setFormElement("url", "http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
    }

    public void testRunnerForAllBrowsers() throws Exception {
        setUpRunnerSubmission();
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                "http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html",
                2
        );
    }

    public void testRunnerWithHTMLSkin() throws Exception {
        setUpRunnerSubmission();
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.selectOption("skinId", "simple_html");
        webTester.submit();
        webTester.assertTitleEquals("JsUnit Test Results");
        webTester.assertTextPresent("http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
//        webTester.assertTextPresent(ResultType.SUCCESS.getDisplayString());
    }

    public void xtestRunnerWithTextSkin() throws Exception {
        setUpRunnerSubmission();
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.selectOption("skinId", "simple_text");
        webTester.submit();
        String responseText = webTester.getDialog().getResponseText();
        assertTrue(responseText.indexOf("http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:") != -1);
        assertTrue(responseText.indexOf(ResultType.SUCCESS.getDisplayString()) != -1);
    }

    public void testDisplayerForm() throws Exception {
        server.launchBrowserTestRun(new BrowserLaunchSpecification(new Browser(Browser.DEFAULT_SYSTEM_BROWSER, 0)));
        BrowserResult browserResult = new BrowserResult();
        String id = String.valueOf(System.currentTimeMillis());
        browserResult.setId(id);
        server.accept(browserResult);
        webTester.beginAt("/");
        webTester.setWorkingForm("displayerForm");
        webTester.setFormElement("id", id);
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.submit();
        assertEquals(XmlUtility.asString(new Document(browserResult.asXml())), webTester.getDialog().getResponseText());
    }

    public void testPasteInFragment() throws Exception {
        webTester.beginAt("/");
        webTester.setWorkingForm("fragmentRunnerForm");
        webTester.setFormElement("fragment", "assertTrue(true);");
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                null,
                2
        );
    }

    public void testPasteInFailingFragmentSingleBrowser() throws Exception {
        webTester.beginAt("/");
        webTester.setWorkingForm("fragmentRunnerForm");
        webTester.setFormElement("fragment", "assertTrue(false);");
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.FAILURE,
                null,
                1
        );
    }

    private void assertOnLandingPage() {
        webTester.assertTitleEquals("JsUnit  Server");
    }

}
