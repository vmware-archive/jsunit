package net.jsunit;

import com.meterware.httpunit.UploadFileSpec;
import com.meterware.httpunit.WebForm;
import net.jsunit.model.*;
import net.jsunit.uploaded.TestPageFactory;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.SystemUtility;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServerLandingPageFunctionalTest extends FunctionalTestCase {

    private List<File> createdFiles = new ArrayList<File>();

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

    public void testIndex() throws Exception {
        webTester.beginAt("/index");
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
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.selectOption("skinId", "HTML");
        webTester.submit();
        webTester.assertTitleEquals("JsUnit Test Results");
        webTester.assertTextPresent("http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html");
//        webTester.assertTextPresent(ResultType.SUCCESS.getDisplayString());
    }

    public void testRunnerWithTextSkin() throws Exception {
        setUpURLRunnerSubmission();
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.selectOption("skinId", "Text");
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
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.submit();
        assertEquals(XmlUtility.asString(new Document(browserResult.asXml())), webTester.getDialog().getResponseText());
    }

    public void testPasteInFragment() throws Exception {
        webTester.beginAt("/");
        webTester.setWorkingForm("fragmentRunnerForm");
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
        webTester.beginAt("/");
        webTester.setWorkingForm("fragmentRunnerForm");
        webTester.setFormElement("fragment", "assertTrue(false);");
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.FAILURE,
                null,
                1
        );
    }

    public void testUploadSuccessfulPageSingleBrowser() throws Exception {
        File file = saveTestPageLocally("assertTrue(true);");
        webTester.beginAt("/");
        webTester.setWorkingForm("uploadRunnerForm");
        WebForm form = webTester.getDialog().getForm();
        form.setParameter("testPageFile", new UploadFileSpec[]{new UploadFileSpec(file)});
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                null,
                1
        );
    }

    public void testUploadFailingPageBothBrowsers() throws Exception {
        File file = saveTestPageLocally("assertTrue(false);");
        webTester.beginAt("/");
        webTester.setWorkingForm("uploadRunnerForm");
        WebForm form = webTester.getDialog().getForm();
        form.setParameter("testPageFile", new UploadFileSpec[]{new UploadFileSpec(file)});
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.FAILURE,
                null,
                2
        );
    }

    public void testUploadWithReferencedJsFiles() throws Exception {
        File referencedJsFile1 = new File("scratch", "testReferencedJs1" + System.currentTimeMillis() + ".js");
        File referencedJsFile2 = new File("scratch", "testReferencedJs2" + System.currentTimeMillis() + ".js");
        FileUtility.write(referencedJsFile1, "function trueFunction() {return true;}");
        FileUtility.write(referencedJsFile2, "function anotherTrueFunction() {return true;}");

        File testPageFile = new File("scratch", "testPage" + System.currentTimeMillis() + ".html");
        String html =
                "<html><head>" +
                        "<script language=\"javascript\" src=\"/my/own/jsUnitCore.js\"></script>" +
                        "<script language=\"javascript\" src=\"" + referencedJsFile1.getName() + "\"></script>" +
                        "<script language=\"javascript\" src=\"" + referencedJsFile2.getName() + "\"></script>" +
                        "<script language=\"javascript\">function testSimple() {assertTrue(trueFunction()); assertTrue(anotherTrueFunction());}" +
                        "</script></head><body></body></html>";
        FileUtility.write(testPageFile, html);

        createdFiles.add(referencedJsFile1);
        createdFiles.add(referencedJsFile2);
        createdFiles.add(testPageFile);

        webTester.beginAt("/index?referencedJsFileFieldCount=2");
        webTester.setWorkingForm("uploadRunnerForm");
        WebForm form = webTester.getDialog().getForm();
        form.setParameter("testPageFile", new UploadFileSpec[]{new UploadFileSpec(testPageFile)});
        form.setParameter(
                "referencedJsFiles",
                new UploadFileSpec[]{new UploadFileSpec(referencedJsFile1), new UploadFileSpec(referencedJsFile2)}
        );
        webTester.selectOption("skinId", "None (raw XML)");

        webTester.submit();
        assertRunResult(
                responseXmlDocument(),
                ResultType.SUCCESS,
                null,
                2
        );
    }

    private File saveTestPageLocally(String fragment) {
        File testPageFile = new File("scratch", "testTestPage_" + System.currentTimeMillis() + ".html");
        TestPage page = new TestPageFactory().fromFragment(fragment);
        FileUtility.write(testPageFile, page.getHtml());
        createdFiles.add(testPageFile);
        return testPageFile;
    }

    public void tearDown() throws Exception {
        for (File file : createdFiles)
            if (file != null)
                file.delete();
        super.tearDown();
    }

}
