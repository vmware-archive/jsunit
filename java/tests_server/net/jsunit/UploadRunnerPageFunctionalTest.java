package net.jsunit;

import com.meterware.httpunit.UploadFileSpec;
import com.meterware.httpunit.WebForm;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.DistributedTestRunResultBuilder;
import net.jsunit.model.TestRunResult;
import net.jsunit.uploaded.UploadedTestPage;
import net.jsunit.uploaded.UploadedTestPageFactory;
import net.jsunit.utility.FileUtility;
import org.jdom.Document;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UploadRunnerPageFunctionalTest extends AggregateServerFunctionalTestCase {

    private List<File> createdFiles = new ArrayList<File>();

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/uploadrunnerpage?referencedJsFileFieldCount=2");
        mockHitter.urlsPassed.clear();
    }

    public void testInitialConditions() throws Exception {
        assertOnUploadRunnerPage();
    }

    public void testUploadSuccessfulPageSingleBrowser() throws Exception {
        mockHitter.setDocumentRetrievalStrategy(new DocumentRetrievalStrategy() {
            public Document get(URL url) {
                return new TestRunResult().asXmlDocument();
            }
        });
        File file = saveTestPageLocally("assertTrue(true);");
        WebForm form = webTester.getDialog().getForm();
        form.setParameter("testPageFile", new UploadFileSpec[]{new UploadFileSpec(file)});
        webTester.setFormElement("urlId_browserId", "0_0");
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        assertEquals(1, mockHitter.urlsPassed.size());
        DistributedTestRunResult result = new DistributedTestRunResultBuilder().build(responseXmlDocument());
        assertEquals(1, result._getTestRunResults().size());
    }

    public void testUploadFailingPage() throws Exception {
        mockHitter.setDocumentRetrievalStrategy(new DocumentRetrievalStrategy() {
            public Document get(URL url) {
                return new DummyFailedTestRunResult().asXmlDocument();
            }
        });
        File file = saveTestPageLocally("assertTrue(false);");
        WebForm form = webTester.getDialog().getForm();
        form.setParameter("testPageFile", new UploadFileSpec[]{new UploadFileSpec(file)});
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        DistributedTestRunResult result = new DistributedTestRunResultBuilder().build(responseXmlDocument());
        assertEquals(2, result._getTestRunResults().size());
        assertFalse(result.wasSuccessful());
    }

    public void testUploadWithReferencedJsFiles() throws Exception {
        mockHitter.setDocumentRetrievalStrategy(new DocumentRetrievalStrategy() {
            public Document get(URL url) {
                return new TestRunResult().asXmlDocument();
            }
        });
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
        DistributedTestRunResult result = new DistributedTestRunResultBuilder().build(responseXmlDocument());
        assertTrue(result.wasSuccessful());
        assertEquals(2, result._getTestRunResults().size());
    }

    private File saveTestPageLocally(String fragment) {
        File testPageFile = new File("scratch", "testTestPage_" + System.currentTimeMillis() + ".html");
        UploadedTestPage page = new UploadedTestPageFactory().fromFragment(fragment);
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
