package net.jsunit;

import net.jsunit.client.TestRunClient;
import net.jsunit.model.*;
import net.jsunit.services.MockUserRepository;
import net.jsunit.utility.XmlUtility;
import net.jsunit.configuration.Configuration;
import org.apache.axis.AxisFault;
import org.jdom.Document;

import java.io.File;
import java.net.URL;

public class ClientFunctionalTest extends AggregateServerFunctionalTestCase {
    private String directory;
    private DummyTestPageWriter writer;

    public void setUp() throws Exception {
        super.setUp();
        directory = String.valueOf(System.currentTimeMillis());
        writer = new DummyTestPageWriter(directory, "a test page.html");
        writer.writeFiles();
        mockHitter.setDocumentRetrievalStrategy(new DocumentRetrievalStrategy() {
            public Document get(URL url) {
                return url.toString().startsWith(FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_1) ?
                        testRunResult0().asXmlDocument() : testRunResult1().asXmlDocument();
            }
        });
        aggregateServer().setUserRepository(new MockUserRepository());
    }

    public void tearDown() throws Exception {
        writer.removeFiles();
        super.tearDown();
    }

    public void testSimple() throws Exception {
        File file = new File(directory, "a test page.html");
        TestRunClient client = new TestRunClient("http://localhost:" + port + "/services/TestRunService");
        client.addBrowserSpec(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.INTERNET_EXPLORER));
        client.addBrowserSpec(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.OPERA));
        client.addBrowserSpec(new BrowserSpecification(PlatformType.LINUX, BrowserType.FIREFOX));
        client.setUsername(MockUserRepository.VALID_USERNAME);
        client.setPassword(MockUserRepository.VALID_PASSWORD);
        DistributedTestRunResult distributedTestRunResult = client.send(file);
        assertFalse(distributedTestRunResult.wasSuccessful());
        assertEquals(2, distributedTestRunResult._getTestRunResults().size());
        assertEquals(XmlUtility.asPrettyString(expectedDistributedTestRunResult().asXml()), XmlUtility.asPrettyString(distributedTestRunResult.asXml()));
        System.out.println(XmlUtility.asPrettyString(expectedDistributedTestRunResult().asXml()));
    }

    public void testInvalidAuthentication() throws Exception {
        File file = new File(directory, "a test page.html");
        TestRunClient client = new TestRunClient("http://localhost:" + port + "/services/TestRunService");
        client.setUsername("bad username");
        client.setPassword("bad password");
        try {
            client.send(file);
            fail();
        } catch (AxisFault fault) {
            assertTrue(fault.getFaultString().indexOf(AuthenticationException.class.getName()) != -1);
        }
    }

    private DistributedTestRunResult expectedDistributedTestRunResult() {
        DistributedTestRunResult result = new DistributedTestRunResult();
        result.addTestRunResult(testRunResult0());
        result.addTestRunResult(testRunResult1());
        return result;
    }

    private TestRunResult testRunResult0() {
        TestRunResult result = new TestRunResult();
        result.addBrowserResult(browserResult0());
        result.addBrowserResult(browserResult1());
        result.setHostname("myhost.mycompany.com");
        result.setIpAddress("123.456.78.9");
        result.setOsName("Windows NT");
        result.setUrl(FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_1);
        return result;
    }

    private TestRunResult testRunResult1() {
        TestRunResult result = new TestRunResult();
        result.addBrowserResult(browserResult0());
        result.addBrowserResult(browserResult1());
        result.setHostname("myhost.mycompany.com");
        result.setIpAddress("123.456.78.9");
        result.setOsName("Windows NT");
        result.setUrl(FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_2);
        return result;
    }

    private BrowserResult browserResult0() {
        BrowserResult result = new BrowserResult();
        result.setBrowser(new Browser("browser0.exe;stop_browser0.sh;My cool browser", 0));
        result.setId("123");
        result.setJsUnitVersion("8.3");
        result.setRemoteAddress("http://www.example.org");
        result.setTime(12.34);
        result.setUserAgent("Mozilla 0.9");
        result._setTestCaseStrings(new String[]{"file:///dummy%20path/dummyPage.html:testFoo|1.3|S||", "file:///dummy%20path/dummyPage.html:testBar|2.3|F||Failed!", "file:///dummy%20path/dummyPage.html:testBaz|12.32|E||crashed!"});
        return result;
    }

    private BrowserResult browserResult1() {
        BrowserResult result = new BrowserResult();
        result.setBrowser(new Browser("browser1.exe;kill_browser1.bat;Kickass browser", 1));
        result.setId("456");
        result.setJsUnitVersion("1.2");
        result.setRemoteAddress("http://www.example.net");
        result.setTime(56.78);
        result.setUserAgent("IE 3.1");
        result._setTestCaseStrings(new String[]{"file:///dummy%20path/dummyPage.html:testFoo|1.1|S||", "file:///dummy%20path/dummyPage.html:testBar|4.5|F||Failed!", "file:///dummy%20path/dummyPage.html:testBaz|34.54|E||crashed!"});
        return result;
    }

}
