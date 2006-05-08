package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.MockRemoteServerHitter;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.Result;
import net.jsunit.model.TestCaseResult;
import net.jsunit.model.TestRunResult;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

import java.io.File;
import java.util.List;

public class TestRunClientTest extends TestCase {
    private DummyTestPageWriter writer;
    public static final String TEST_PAGE_FILE_NAME = "myTestPage.html";
    private String directory;

    protected void setUp() throws Exception {
        super.setUp();
        directory = String.valueOf(System.currentTimeMillis());
        writer = new DummyTestPageWriter(directory, TEST_PAGE_FILE_NAME);
        writer.writeFiles();
    }

    protected void tearDown() throws Exception {
        writer.removeFiles();
        super.tearDown();
    }

    public void testBadServiceURL() throws Exception {
        try {
            new TestRunClient("not a url");
            fail();
        } catch (RuntimeException e) {
        }
    }

    public void testSimple() throws Exception {
        MockRemoteServerHitter mockHitter = new MockRemoteServerHitter();
        TestRunResult testRunResult = dummyResult();
        mockHitter.urlToDocument.put("http://server.jsunit.net/runner", new Document(testRunResult.asXml()));
        TestRunClient client = new TestRunClient("http://server.jsunit.net/runner", mockHitter);
        File page = new File(directory, TEST_PAGE_FILE_NAME);
        Result result = client.send(page);
        String expectedXML = XmlUtility.asString(testRunResult.asXml());
        assertEquals(expectedXML, XmlUtility.asString(result.asXml()));

        assertEquals(1, mockHitter.urlsPassed.size());
        assertEquals("http://server.jsunit.net/runner", mockHitter.urlsPassed.get(0));
        assertEquals(1, mockHitter.fieldsToValuesMapsPosted.size());

        List<File> testPageFiles = mockHitter.fieldsToValuesMapsPosted.get(0).get("testPageFile");
        assertEquals(1, testPageFiles.size());
        assertEquals(TEST_PAGE_FILE_NAME, testPageFiles.get(0).getName());

        List<File> referencedJsFiles = mockHitter.fieldsToValuesMapsPosted.get(0).get("referencedJsFiles");
        assertEquals(2, referencedJsFiles.size());
        assertEquals("file1.js", referencedJsFiles.get(0).getName());
        assertEquals("file2.js", referencedJsFiles.get(1).getName());
    }

    private TestRunResult dummyResult() {
        TestRunResult result = new TestRunResult();
        result.addBrowserResult(successfulBrowserResult());
        result.addBrowserResult(errorBrowserResult());
        return result;
    }

    private BrowserResult successfulBrowserResult() {
        BrowserResult browserResult = new BrowserResult();
        TestCaseResult testCaseResult = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testBar|1.3|S||");
        browserResult.addTestCaseResult(testCaseResult);
        return browserResult;
    }

    private BrowserResult errorBrowserResult() {
        BrowserResult browserResult = new BrowserResult();
        TestCaseResult testCaseResult = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|");
        browserResult.addTestCaseResult(testCaseResult);
        return browserResult;
    }

}
