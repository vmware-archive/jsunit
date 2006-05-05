package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.model.*;
import net.jsunit.server.RemoteRunSpecificationBuilder;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class DistributedTestRunManagerTest extends TestCase {

    private Configuration configuration;
    public static final String SECRET_KEY = "1234567890123456";

    public void setUp() throws Exception {
        super.setUp();
        configuration = new Configuration(new DummyConfigurationSource());
    }

    public void testSimple() throws MalformedURLException, UnsupportedEncodingException {
        String encodedURL = URLEncoder.encode(DummyConfigurationSource.DUMMY_URL, "UTF-8");
        String url1 = DummyConfigurationSource.REMOTE_URL_1 + "/runner?url=" + encodedURL;
        String url2 = DummyConfigurationSource.REMOTE_URL_2 + "/runner?url=" + encodedURL;
        MockRemoteServerHitter hitter = createMockHitter(url1, url2);
        List<RemoteRunSpecification> specs =
                fullRunSpecsFor(DummyConfigurationSource.REMOTE_URL_1, DummyConfigurationSource.REMOTE_URL_2);
        DistributedTestRunManager manager = new DistributedTestRunManager(hitter, configuration, null, specs);
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        assertTrue(hitter.urlsPassed.contains(url1));
        assertTrue(hitter.urlsPassed.contains(url2));
        DistributedTestRunResult result = manager.getDistributedTestRunResult();

        DistributedTestRunResult expectedResult = new DistributedTestRunResult();
        expectedResult.addTestRunResult(createResult1());
        expectedResult.addTestRunResult(createResult2());

        assertEquals(XmlUtility.asString(expectedResult.asXml()), XmlUtility.asString(result.asXml()));
    }

    public void testRemoteURLBlowsUp() throws MalformedURLException {
        DistributedTestRunManager manager = new DistributedTestRunManager(
                new BlowingUpRemoteServerHitter(), configuration, null,
                fullRunSpecsFor(DummyConfigurationSource.REMOTE_URL_1, DummyConfigurationSource.REMOTE_URL_2)
        );
        assertFalse(configuration.shouldIgnoreUnresponsiveRemoteMachines());
        manager.runTests();
        DistributedTestRunResult result = manager.getDistributedTestRunResult();
        assertFalse(result.wasSuccessful());
        List<TestRunResult> testRunResults = result.getTestRunResults();
        assertEquals(2, testRunResults.size());
        assertEquals(ResultType.UNRESPONSIVE, testRunResults.get(0).getResultType());
        assertEquals(DummyConfigurationSource.REMOTE_URL_1, testRunResults.get(0).getUrl().toString());
        assertEquals(DummyConfigurationSource.REMOTE_URL_2, testRunResults.get(1).getUrl().toString());
        assertEquals(ResultType.UNRESPONSIVE, testRunResults.get(1).getResultType());
    }

    public void testRemoteURLBlowsUpButIgnored() throws MalformedURLException {
        configuration = new Configuration(new DummyConfigurationSource() {
            public String ignoreUnresponsiveRemoteMachines() {
                return "true";
            }
        });
        assertTrue(configuration.shouldIgnoreUnresponsiveRemoteMachines());
        DistributedTestRunManager manager = new DistributedTestRunManager(
                new BlowingUpRemoteServerHitter(), configuration, null,
                fullRunSpecsFor(DummyConfigurationSource.REMOTE_URL_1, DummyConfigurationSource.REMOTE_URL_2)
        );
        manager.runTests();
        DistributedTestRunResult result = manager.getDistributedTestRunResult();
        assertTrue(result.wasSuccessful());
        assertEquals(0, result.getTestRunResults().size());
    }

    public void testOverrideURL() throws Exception {
        String overrideURL = "http://my.override.com:1234?foo=bar&bar=foo";
        String encodedOverrideURL = URLEncoder.encode(overrideURL, "UTF-8");
        String url1 = DummyConfigurationSource.REMOTE_URL_1 + "/runner?url=" + encodedOverrideURL;
        String url2 = DummyConfigurationSource.REMOTE_URL_2 + "/runner?url=" + encodedOverrideURL;
        MockRemoteServerHitter hitter = createMockHitter(url1, url2);
        DistributedTestRunManager manager = new DistributedTestRunManager(
                hitter, configuration, overrideURL,
                fullRunSpecsFor(DummyConfigurationSource.REMOTE_URL_1, DummyConfigurationSource.REMOTE_URL_2)
        );
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        assertTrue(hitter.urlsPassed.contains(url1));
        assertTrue(hitter.urlsPassed.contains(url2));
    }

    public void testNoURL() throws Exception {
        configuration = new Configuration(new DummyConfigurationSource() {
            public String url() {
                return null;
            }
        });
        String url1 = DummyConfigurationSource.REMOTE_URL_1 + "/runner";
        String url2 = DummyConfigurationSource.REMOTE_URL_2 + "/runner";
        MockRemoteServerHitter hitter = createMockHitter(url1, url2);

        DistributedTestRunManager manager = new DistributedTestRunManager(
                hitter, configuration, null, fullRunSpecsFor(DummyConfigurationSource.REMOTE_URL_1, DummyConfigurationSource.REMOTE_URL_2)
        );
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        assertTrue(hitter.urlsPassed.contains(url1));
        assertTrue(hitter.urlsPassed.contains(url2));
        DistributedTestRunResult result = manager.getDistributedTestRunResult();

        DistributedTestRunResult expectedResult = new DistributedTestRunResult();
        expectedResult.addTestRunResult(createResult1());
        expectedResult.addTestRunResult(createResult2());

        assertEquals(XmlUtility.asString(expectedResult.asXml()), XmlUtility.asString(result.asXml()));
    }

    public void testRunSpecsWithSpecificBrowsers() throws Exception {
        String overrideURL = "http://my.override.com:1234?foo=bar&bar=foo";
        String encodedOverrideURL = URLEncoder.encode(overrideURL, "UTF-8");
        String url1 = DummyConfigurationSource.REMOTE_URL_1 + "/runner?url=" + encodedOverrideURL + "&browserId=0&browserId=2";
        String url2 = DummyConfigurationSource.REMOTE_URL_2 + "/runner?url=" + encodedOverrideURL + "&browserId=2";
        MockRemoteServerHitter hitter = createMockHitter(url1, url2);
        List<RemoteRunSpecification> specs = new ArrayList<RemoteRunSpecification>();
        RemoteRunSpecification spec0 = new RemoteRunSpecification(new URL(DummyConfigurationSource.REMOTE_URL_1));
        spec0.addBrowser(new Browser("browser0.exe", 0));
        spec0.addBrowser(new Browser("browser2.exe", 2));
        RemoteRunSpecification spec1 = new RemoteRunSpecification(new URL(DummyConfigurationSource.REMOTE_URL_2));
        spec1.addBrowser(new Browser("browser2.exe", 2));
        specs.add(spec0);
        specs.add(spec1);
        DistributedTestRunManager manager = new DistributedTestRunManager(
                hitter, configuration, overrideURL,
                specs
        );
        manager.runTests();
        assertEquals(2, hitter.urlsPassed.size());
        assertTrue(hitter.urlsPassed.contains(url1));
        assertTrue(hitter.urlsPassed.contains(url2));
    }

    public void testDistributedResultReturned() throws Exception {
        String encodedURL = URLEncoder.encode(DummyConfigurationSource.DUMMY_URL, "UTF-8");
        String url1 = DummyConfigurationSource.REMOTE_URL_1 + "/runner?url=" + encodedURL;
        String url2 = DummyConfigurationSource.REMOTE_URL_2 + "/runner?url=" + encodedURL;
        MockRemoteServerHitter hitter = createMockHitterWithDistributedResults(url1, url2);
        DistributedTestRunManager manager = new DistributedTestRunManager(
                hitter, configuration, null,
                fullRunSpecsFor(DummyConfigurationSource.REMOTE_URL_1, DummyConfigurationSource.REMOTE_URL_2)
        );
        manager.runTests();
        DistributedTestRunResult result = manager.getDistributedTestRunResult();
        List<TestRunResult> results = result.getTestRunResults();
        assertEquals(4, results.size());
        Collections.sort(results, new Comparator<TestRunResult>() {
            public int compare(TestRunResult o1, TestRunResult o2) {
                return o1.getUrl().toString().compareTo(o2.getUrl().toString());
            }
        });
        assertEquals(DummyConfigurationSource.REMOTE_URL_1, results.get(0).getUrl().toString());
        assertEquals(DummyConfigurationSource.REMOTE_URL_1, results.get(1).getUrl().toString());
        assertEquals(DummyConfigurationSource.REMOTE_URL_2, results.get(2).getUrl().toString());
        assertEquals(DummyConfigurationSource.REMOTE_URL_2, results.get(3).getUrl().toString());
    }

    public void testUseCaptcha() throws Exception {
        DummyConfigurationSource source = new DummyConfigurationSource() {
            public String useCaptcha() {
                return String.valueOf(true);
            }
        };
        configuration = new Configuration(source) {
            public String getSecretKey() {
                return SECRET_KEY;
            }
        };
        MockRemoteServerHitter hitter = new MockRemoteServerHitter();
        DistributedTestRunManager manager = new DistributedTestRunManager(
                hitter, configuration, null,
                fullRunSpecsFor("http://www.example.com")
        );
        manager.runTests();
        String urlHit = hitter.urlsPassed.get(0);
        assertTrue(urlHit.indexOf("captchaKey") != -1);
        assertTrue(urlHit.indexOf("attemptedCaptchaAnswer") != -1);
    }

    private MockRemoteServerHitter createMockHitter(String url1, String url2) throws MalformedURLException {
        MockRemoteServerHitter hitter = new MockRemoteServerHitter();
        hitter.urlToDocument.put(url1, new Document(createResult1().asXml()));
        hitter.urlToDocument.put(url2, new Document(createResult2().asXml()));
        return hitter;
    }

    private MockRemoteServerHitter createMockHitterWithDistributedResults(String url1, String url2) throws MalformedURLException {
        MockRemoteServerHitter hitter = new MockRemoteServerHitter();
        DistributedTestRunResult distributedResult = new DistributedTestRunResult();
        distributedResult.addTestRunResult(createResult1());
        distributedResult.addTestRunResult(createResult2());
        hitter.urlToDocument.put(url1, new Document(distributedResult.asXml()));
        hitter.urlToDocument.put(url2, new Document(distributedResult.asXml()));
        return hitter;
    }

    private TestRunResult createResult1() throws MalformedURLException {
        TestRunResult result = new TestRunResult(new URL(DummyConfigurationSource.REMOTE_URL_1));
        result.setOsName("my os");
        BrowserResult browserResult1 = new BrowserResult();
        browserResult1.setId("1");
        browserResult1.setBrowser(new Browser("mybrowser.exe", 0));
        browserResult1.setTime(123.45);
        result.addBrowserResult(browserResult1);

        BrowserResult browserResult2 = new BrowserResult();
        browserResult2.setId("2");
        browserResult2.setBrowser(new Browser("mybrowser.exe", 0));
        browserResult2.setResultType(ResultType.FAILED_TO_LAUNCH);
        result.addBrowserResult(browserResult2);

        return result;
    }

    private TestRunResult createResult2() throws MalformedURLException {
        TestRunResult result = new TestRunResult(new URL(DummyConfigurationSource.REMOTE_URL_2));
        result.setOsName("my other os");
        BrowserResult browserResult1 = new BrowserResult();
        browserResult1.setBrowser(new Browser("mybrowser.exe", 0));
        browserResult1.setId("a");
        browserResult1.setTime(123.45);
        browserResult1.setBaseURL("http://www.example.com");
        browserResult1.setId("12345");
        browserResult1.setUserAgent("foo bar");
        result.addBrowserResult(browserResult1);

        BrowserResult browserResult2 = new BrowserResult();
        browserResult1.setId("b");
        browserResult2.setBrowser(new Browser("mybrowser.exe", 0));
        browserResult2.setResultType(ResultType.FAILED_TO_LAUNCH);
        result.addBrowserResult(browserResult2);

        return result;
    }

    private List<RemoteRunSpecification> fullRunSpecsFor(String... strings) throws MalformedURLException {
        RemoteRunSpecificationBuilder builder = new RemoteRunSpecificationBuilder();
        URL[] urls = new URL[strings.length];
        for (int i = 0; i < strings.length; i++)
            urls[i] = new URL(strings[i]);
        return builder.forAllBrowsersFromRemoteURLs(urls);
    }

}
