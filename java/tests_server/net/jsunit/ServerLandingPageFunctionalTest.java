package net.jsunit;

import com.meterware.httpunit.UploadFileSpec;
import com.meterware.httpunit.WebForm;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.ResultType;
import net.jsunit.upload.TestPageGenerator;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.SystemUtility;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileNotFoundException;

public class ServerLandingPageFunctionalTest extends FunctionalTestCase {
    private File createdFile;

    protected boolean needsRealResultRepository() {
        return true;
    }

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    public void testSlash() throws Exception {
        webTester.beginAt("/");
        assertOnLandingPage();
        webTester.assertTextPresent(SystemUtility.osString());
        webTester.assertTextPresent(SystemUtility.hostname());
        webTester.assertTextPresent(SystemUtility.ipAddress());
    }

    public void testIndexDotJsp() throws Exception {
        webTester.beginAt("/index.jsp");
        assertOnLandingPage();
    }

    public void testRunnerForParticularBrowser() throws Exception {
        setUpURLRunnerSubmission();
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                "http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html",
                1
        );
    }

    private void setUpURLRunnerSubmission() {
        webTester.beginAt("/");
        webTester.setWorkingForm("urlRunnerForm");
        webTester.setFormElement("url", "http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
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
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.selectOption("skinId", "simple_html");
        webTester.submit();
        webTester.assertTitleEquals("JsUnit Test Results");
        webTester.assertTextPresent("http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
//        webTester.assertTextPresent(ResultType.SUCCESS.getDisplayString());
    }

    public void testRunnerWithTextSkin() throws Exception {
        setUpURLRunnerSubmission();
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.selectOption("skinId", "simple_text");
        webTester.submit();
//        String responseText = webTester.getDialog().getResponseText();
//        assertTrue(responseText.indexOf("http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:") != -1);
//        assertTrue(responseText.indexOf(ResultType.SUCCESS.getDisplayString()) != -1);
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

    public void testUploadSuccessfulPageSingleBrowser() throws Exception {
        File file = saveTestLocally("assertTrue(true);");
        webTester.beginAt("/");
        webTester.setWorkingForm("uploadRunnerForm");
        WebForm form = webTester.getDialog().getForm();
        form.setParameter("testPageFile", new UploadFileSpec[]{new UploadFileSpec(file)});
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                null,
                1
        );
    }

    public void testUploadFailingPageBothBrowsers() throws Exception {
        File file = saveTestLocally("assertTrue(false);");
        webTester.beginAt("/");
        webTester.setWorkingForm("uploadRunnerForm");
        webTester.setWorkingForm("uploadRunnerForm");
        WebForm form = webTester.getDialog().getForm();
        form.setParameter("testPageFile", new UploadFileSpec[]{new UploadFileSpec(file)});
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.FAILURE,
                null,
                2
        );
    }

    private File saveTestLocally(String fragment) throws FileNotFoundException, TransformerException {
        createdFile = new File("scratch", "testTestPage_" + System.currentTimeMillis() + ".html");
        String contents = new TestPageGenerator().generateHtmlFrom(fragment);
        FileUtility.write(createdFile, contents);
        return createdFile;
    }

    private void assertOnLandingPage() {
        webTester.assertTitleEquals("JsUnit  Server");
    }

    public void tearDown() throws Exception {
        if (createdFile != null)
            createdFile.delete();
        super.tearDown();
    }

}
