package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.client.TestRunClient;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.ServerType;
import net.jsunit.model.*;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

            public String remoteMachineURLs() {
                return "http://localhost:1,http://localhost:2,http://localhost:3";
            }
        };
        mockHitter = new MockRemoteServerHitter();
        Document remoteConfigurationDocument = new Configuration(new DummyConfigurationSource()).asXmlDocument(ServerType.STANDARD);
        mockHitter.urlToDocument.put("http://localhost:1/jsunit/config", remoteConfigurationDocument);
        mockHitter.urlToDocument.put("http://localhost:2/jsunit/config", remoteConfigurationDocument);
        mockHitter.urlToDocument.put("http://localhost:3/jsunit/config", remoteConfigurationDocument);
        server = new JsUnitAggregateServer(new Configuration(source), mockHitter);
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
        mockHitter.urlsPassed.clear();
        TestRunClient client = new TestRunClient("http://localhost:" + port + "/axis/services/TestRunService");
        File page = new File(directory, TEST_PAGE_FILE_NAME);
        Result result = client.send(page);
        assertEquals(3, mockHitter.urlsPassed.size());
        assertTrue(mockHitter.urlsPassed.contains("http://localhost:1/jsunit/runner?url=" + testURL()));
        assertTrue(mockHitter.urlsPassed.contains("http://localhost:2/jsunit/runner?url=" + testURL()));
        assertTrue(mockHitter.urlsPassed.contains("http://localhost:2/jsunit/runner?url=" + testURL()));
//        assertEquals(XmlUtility.asString(dummyDistributedTestRunResult().asXml()), XmlUtility.asString(result.asXml()));
    }

    private String testURL() throws UnsupportedEncodingException {
        return URLEncoder.encode("http://www.example.com:1234/jsunit/runner?autoRun=true&submitResults=true", "UTF-8");
    }

    private DistributedTestRunResult dummyDistributedTestRunResult() {
        DistributedTestRunResult result = new DistributedTestRunResult();
        result.addTestRunResult(dummyTestRunResult());
        result.addTestRunResult(dummyTestRunResult());
        result.addTestRunResult(dummyTestRunResult());
        return result;
    }

    private TestRunResult dummyTestRunResult() {
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
