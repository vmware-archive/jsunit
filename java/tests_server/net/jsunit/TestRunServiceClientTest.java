package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.client.TestRunClient;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.model.*;
import net.jsunit.utility.XmlUtility;

import java.io.File;

public class TestRunServiceClientTest extends TestCase {
    private DummyTestPageWriter writer;
    public static final String TEST_PAGE_FILE_NAME = "myTestPage.html";
    private String directory;
    private JsUnitAggregateServer server;
    private MockRemoteServerHitter mockHitter;
    private int port;

    protected void setUp() throws Exception {
        super.setUp();
        directory = String.valueOf(System.currentTimeMillis());
        writer = new DummyTestPageWriter(directory, TEST_PAGE_FILE_NAME);
        writer.writeFiles();
        port = new TestPortManager().newPort();
        DummyConfigurationSource source = new DummyConfigurationSource() {
            public String port() {
                return String.valueOf(port);
            }
        };
        server = new JsUnitAggregateServer(new Configuration(source));
        server.start();
    }

    protected void tearDown() throws Exception {
        writer.removeFiles();
        server.dispose();
        super.tearDown();
    }

    public void testBadServiceURL() throws Exception {
        TestRunClient client = new TestRunClient("not a url");
        try {
            client.send(new File("foo"));
            fail();
        } catch (Exception e) {
        }
    }

    public void testSimple() throws Exception {
        TestRunClient client = new TestRunClient("http://localhost:" + port + "/axis/services/TestRunService");
        File page = new File(directory, TEST_PAGE_FILE_NAME);
        try {
            Result result = client.send(page);
        } catch (NullPointerException exception) {
            //TODO
        }
//        assertEquals(XmlUtility.asString(dummyResult().asXml()), XmlUtility.asString(result.asXml()));

/*
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
*/
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
