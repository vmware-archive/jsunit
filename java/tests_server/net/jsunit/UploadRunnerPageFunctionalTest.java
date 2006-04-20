package net.jsunit;

import com.meterware.httpunit.UploadFileSpec;
import com.meterware.httpunit.WebForm;
import net.jsunit.model.Browser;
import net.jsunit.model.ResultType;
import net.jsunit.model.TestPage;
import net.jsunit.uploaded.TestPageFactory;
import net.jsunit.utility.FileUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadRunnerPageFunctionalTest extends FunctionalTestCase {

    private List<File> createdFiles = new ArrayList<File>();

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/uploadRunnerPage?referencedJsFileFieldCount=2");
    }

    public void testInitialConditions() throws Exception {
        assertOnUploadRunnerPage();
    }

    public void testUploadSuccessfulPageSingleBrowser() throws Exception {
        File file = saveTestPageLocally("assertTrue(true);");
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
