package net.jsunit;

import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.DistributedTestRunResultBuilder;
import net.jsunit.model.TestRunResult;
import org.jdom.Document;

import java.net.URL;
import java.net.URLEncoder;

public class UrlRunnerPageFunctionalTest extends AggregateServerFunctionalTestCase {

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/urlrunnerpage");
        mockHitter.urlsPassed.clear();
        mockHitter.setDocumentRetrievalStrategy(new DocumentRetrievalStrategy() {
            public Document get(URL url) {
                return new TestRunResult().asXmlDocument();
            }
        });
    }

    public void testInitialConditions() throws Exception {
        assertOnUrlRunnerPage();
        webTester.assertFormElementEquals("url", "");
    }

    public void testRunnerForParticularBrowser() throws Exception {
        setUpURLRunnerSubmission();
        webTester.checkCheckbox("urlId_browserId", "0_0");
        webTester.submit();
        DistributedTestRunResult result = new DistributedTestRunResultBuilder().build(responseXmlDocument());
        assertTrue(result.wasSuccessful());
        assertEquals(1, result._getTestRunResults().size());
        assertTrue(mockHitter.urlsPassed.get(0).indexOf(URLEncoder.encode(url(), "UTF-8")) != -1);
    }

    public void testRunnerForAllBrowsers() throws Exception {
        setUpURLRunnerSubmission();
        webTester.submit();
        assertEquals(2, mockHitter.urlsPassed.size());
        assertTrue(mockHitter.urlsPassed.get(0).indexOf(URLEncoder.encode(url(), "UTF-8")) != -1);
        assertTrue(mockHitter.urlsPassed.get(1).indexOf(URLEncoder.encode(url(), "UTF-8")) != -1);
    }

    public void testRunnerWithHTMLSkin() throws Exception {
        setUpURLRunnerSubmission();
        webTester.selectOption("skinId", "HTML");
        webTester.submit();
        webTester.gotoFrame("resultsFrame");
        webTester.assertTitleEquals("JsUnit Test Results");
    }

    public void testRunnerWithTextSkin() throws Exception {
        setUpURLRunnerSubmission();
        webTester.checkCheckbox("urlId_browserId", "0_1");
        webTester.submit();
        webTester.gotoFrame("resultsFrame");
//        webTester.assertTitleEquals("JsUnit Test Results");
//        webTester.assertTextPresent("http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
//        webTester.assertTextPresent(ResultType.SUCCESS.name());
    }

    public void testPassingInUrl() throws Exception {
        webTester.beginAt("/urlrunnerpage?url=foobar");
        webTester.assertFormElementEquals("url", "foobar");
    }

    private void setUpURLRunnerSubmission() {
        webTester.setFormElement("url", url());
        webTester.selectOption("skinId", "None (raw XML)");
    }

    private String url() {
        return "http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html";
    }

}
