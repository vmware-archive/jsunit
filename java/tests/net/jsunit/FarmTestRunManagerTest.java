package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.TestRunResult;
import org.jdom.Document;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FarmTestRunManagerTest extends TestCase {

    private Configuration configuration;

    public void setUp() throws Exception {
        super.setUp();
        configuration = new Configuration(new DummyConfigurationSource());
    }

    public void testSimple() throws MalformedURLException, UnsupportedEncodingException {
        MockRemoteRunnerHitter hitter = new MockRemoteRunnerHitter();
        FarmTestRunManager manager = new FarmTestRunManager(hitter, configuration);
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        String encodedURL = URLEncoder.encode(DummyConfigurationSource.DUMMY_URL, "UTF-8");
        assertEquals(DummyConfigurationSource.REMOTE_URL_1 + "jsunit/runner?url=" + encodedURL, hitter.urlsPassed.get(0).toString());
        assertEquals(DummyConfigurationSource.REMOTE_URL_2 + "/jsunit/runner?url=" + encodedURL, hitter.urlsPassed.get(1).toString());
        TestRunResult result = manager.getTestRunResult();

        TestRunResult expectedResult = new TestRunResult();
        expectedResult.mergeWith(createResult1());
        expectedResult.mergeWith(createResult2());

        assertEquals(Utility.asString(expectedResult.asXml()), Utility.asString(result.asXml()));
    }

    public void testRemoteURLBlowsUp() {
        FarmTestRunManager manager = new FarmTestRunManager(new BlowingUpRemoteRunnerHitter(), configuration);
        manager.runTests();
        TestRunResult result = manager.getTestRunResult();
        assertFalse(result.wasSuccessful());
        assertEquals(2, result.getCrashedRemoteURLs().size());
    }

    public void testOverrideURL() throws Exception {
        String overrideURL = "http://my.override.com:1234?foo=bar&bar=foo";
        String encodedOverrideURL = URLEncoder.encode(overrideURL, "UTF-8");
        MockRemoteRunnerHitter hitter = new MockRemoteRunnerHitter();
        FarmTestRunManager manager = new FarmTestRunManager(hitter, configuration, overrideURL);
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        assertEquals(
                DummyConfigurationSource.REMOTE_URL_1 + "jsunit/runner?url=" + encodedOverrideURL,
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
        MockRemoteRunnerHitter hitter = new MockRemoteRunnerHitter();
        FarmTestRunManager manager = new FarmTestRunManager(hitter, configuration);
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        assertEquals(DummyConfigurationSource.REMOTE_URL_1 + "jsunit/runner", hitter.urlsPassed.get(0).toString());
        assertEquals(DummyConfigurationSource.REMOTE_URL_2 + "/jsunit/runner", hitter.urlsPassed.get(1).toString());
        TestRunResult result = manager.getTestRunResult();

        TestRunResult expectedResult = new TestRunResult();
        expectedResult.mergeWith(createResult1());
        expectedResult.mergeWith(createResult2());

        assertEquals(Utility.asString(expectedResult.asXml()), Utility.asString(result.asXml()));
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

    class MockRemoteRunnerHitter implements RemoteRunnerHitter {

        private List<URL> urlsPassed;
        private List<TestRunResult> results;
        private int index = 0;

        public MockRemoteRunnerHitter() {
            results = new ArrayList<TestRunResult>();
            urlsPassed = new ArrayList<URL>();
            results.add(createResult1());
            results.add(createResult2());
        }

        public Document hitRemoteRunner(URL url) {
            urlsPassed.add(url);
            return new Document(results.get(index++).asXml());
        }

    }

    static class BlowingUpRemoteRunnerHitter implements RemoteRunnerHitter {

        public Document hitRemoteRunner(URL url) throws IOException {
            throw new IOException();
        }
    }

}
