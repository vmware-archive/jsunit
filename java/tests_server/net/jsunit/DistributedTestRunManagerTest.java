package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.TestRunResult;
import net.jsunit.utility.XmlUtility;

import org.jdom.Document;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

public class DistributedTestRunManagerTest extends TestCase {

    private Configuration configuration;

    public void setUp() throws Exception {
        super.setUp();
        configuration = new Configuration(new DummyConfigurationSource());
    }

    public void testSimple() throws MalformedURLException, UnsupportedEncodingException {
        MockRemoteRunnerHitter hitter = createMockHitter();
        DistributedTestRunManager manager = new DistributedTestRunManager(hitter, configuration);
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        String encodedURL = URLEncoder.encode(DummyConfigurationSource.DUMMY_URL, "UTF-8");
        assertEquals(DummyConfigurationSource.REMOTE_URL_1 + "/jsunit/runner?url=" + encodedURL, hitter.urlsPassed.get(0).toString());
        assertEquals(DummyConfigurationSource.REMOTE_URL_2 + "/jsunit/runner?url=" + encodedURL, hitter.urlsPassed.get(1).toString());
        TestRunResult result = manager.getTestRunResult();

        TestRunResult expectedResult = new TestRunResult();
        expectedResult.mergeWith(createResult1());
        expectedResult.mergeWith(createResult2());

        assertEquals(XmlUtility.asString(expectedResult.asXml()), XmlUtility.asString(result.asXml()));
    }

    public void testRemoteURLBlowsUp() {
        DistributedTestRunManager manager = new DistributedTestRunManager(new BlowingUpRemoteRunnerHitter(), configuration);
        manager.runTests();
        TestRunResult result = manager.getTestRunResult();
        assertFalse(result.wasSuccessful());
        assertEquals(2, result.getCrashedRemoteURLs().size());
    }

    public void testOverrideURL() throws Exception {
        String overrideURL = "http://my.override.com:1234?foo=bar&bar=foo";
        String encodedOverrideURL = URLEncoder.encode(overrideURL, "UTF-8");
        MockRemoteRunnerHitter hitter = createMockHitter();
        DistributedTestRunManager manager = new DistributedTestRunManager(hitter, configuration, overrideURL);
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        assertEquals(
                DummyConfigurationSource.REMOTE_URL_1 + "/jsunit/runner?url=" + encodedOverrideURL,
                hitter.urlsPassed.get(0).toString());
        assertEquals(
                DummyConfigurationSource.REMOTE_URL_2 + "/jsunit/runner?url=" + encodedOverrideURL,
                hitter.urlsPassed.get(1).toString());
    }

    public void testNoURL() throws Exception {
        configuration = new Configuration(new DummyConfigurationSource() {
            public String url() {
                return null;
            }
        });
        MockRemoteRunnerHitter hitter = createMockHitter();

        DistributedTestRunManager manager = new DistributedTestRunManager(hitter, configuration);
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        assertEquals(DummyConfigurationSource.REMOTE_URL_1 + "/jsunit/runner", hitter.urlsPassed.get(0).toString());
        assertEquals(DummyConfigurationSource.REMOTE_URL_2 + "/jsunit/runner", hitter.urlsPassed.get(1).toString());
        TestRunResult result = manager.getTestRunResult();

        TestRunResult expectedResult = new TestRunResult();
        expectedResult.mergeWith(createResult1());
        expectedResult.mergeWith(createResult2());

        assertEquals(XmlUtility.asString(expectedResult.asXml()), XmlUtility.asString(result.asXml()));
    }

	private MockRemoteRunnerHitter createMockHitter() {
		MockRemoteRunnerHitter hitter = new MockRemoteRunnerHitter();
        hitter.documents.add(new Document(createResult1().asXml()));
        hitter.documents.add(new Document(createResult2().asXml()));
		return hitter;
	}

    private TestRunResult createResult1() {
        TestRunResult result = new TestRunResult();
        BrowserResult browserResult1 = new BrowserResult();
        browserResult1.setId("1");
        browserResult1.setBrowserFileName("mybrowser1.exe");
        browserResult1.setTime(123.45);
        result.addBrowserResult(browserResult1);

        BrowserResult browserResult2 = new BrowserResult();
        browserResult2.setId("2");
        browserResult2.setBrowserFileName("mybrowser2.exe");
        browserResult2.setFailedToLaunch();
        result.addBrowserResult(browserResult2);

        return result;
    }

    private TestRunResult createResult2() {
        TestRunResult result = new TestRunResult();
        BrowserResult browserResult1 = new BrowserResult();
        browserResult1.setBrowserFileName("mybrowser3.exe");
        browserResult1.setId("a");
        browserResult1.setTime(123.45);
        browserResult1.setBaseURL("http://www.example.com");
        browserResult1.setId("12345");
        browserResult1.setUserAgent("foo bar");
        result.addBrowserResult(browserResult1);

        BrowserResult browserResult2 = new BrowserResult();
        browserResult1.setId("b");
        browserResult2.setBrowserFileName("mybrowser4.exe");
        browserResult2.setTimedOut();
        result.addBrowserResult(browserResult2);

        return result;
    }

}
