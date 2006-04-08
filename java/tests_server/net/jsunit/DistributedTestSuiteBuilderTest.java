package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import org.jdom.Document;

public class DistributedTestSuiteBuilderTest extends TestCase {
    private DistributedTestSuiteBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        DummyConfigurationSource originalSource = new DummyConfigurationSource();
        MockRemoteServerHitter mockHitter = new MockRemoteServerHitter();
        originalSource.setNeeds3rdRemoteMachineURL();
        mockHitter.urlToDocument.put(DummyConfigurationSource.REMOTE_URL_1 + "/config", remoteConfiguration1XmlDocument());
        mockHitter.urlToDocument.put(DummyConfigurationSource.REMOTE_URL_2 + "/config", remoteConfiguration2XmlDocument());
        mockHitter.urlToDocument.put(DummyConfigurationSource.REMOTE_URL_3 + "/config", remoteConfiguration3XmlDocument());
        builder = new DistributedTestSuiteBuilder(originalSource, mockHitter);
    }

    public void testBuildSuite() throws Exception {
        TestSuite aSuite = new TestSuite();
        builder.addTestsTo(aSuite);

        assertEquals(3, builder.getRemoteMachineURLCount());
        assertEquals(5, builder.getBrowserCount());
        assertEquals("JsUnit Tests (3 machines, 5 direct browsers)", aSuite.getName());

        assertEquals(3, aSuite.testCount());

        DistributedTest testForURL3 = (DistributedTest) aSuite.testAt(0);
        assertEquals("his\u00B7machine\u00B7com:7070 - aggregate server with 2 remote machine(s)", testForURL3.getName());

        TestSuite suiteForURL1 = (TestSuite) aSuite.testAt(1);
        assertEquals("my\u00B7machine\u00B7com:8080 - server with 2 browser(s)", suiteForURL1.getName());
        assertEquals(2, suiteForURL1.testCount());
        DistributedTest testForBrowser1OnURL1 = (DistributedTest) suiteForURL1.testAt(0);
        DistributedTest testForBrowser2OnURL1 = (DistributedTest) suiteForURL1.testAt(1);
        assertEquals("browser1.exe", testForBrowser1OnURL1.getName());
        assertEquals("browser2.exe", testForBrowser2OnURL1.getName());

        TestSuite suiteForURL2 = (TestSuite) aSuite.testAt(2);
        assertEquals("your\u00B7machine\u00B7com:9090 - server with 3 browser(s)", suiteForURL2.getName());
        assertEquals(3, suiteForURL2.testCount());
        DistributedTest testForBrowser3OnURL2 = (DistributedTest) suiteForURL2.testAt(0);
        DistributedTest testForBrowser4OnURL2 = (DistributedTest) suiteForURL2.testAt(1);
        DistributedTest testForBrowser5OnURL2 = (DistributedTest) suiteForURL2.testAt(2);
        assertEquals("browser3.exe", testForBrowser3OnURL2.getName());
        assertEquals("browser4.exe", testForBrowser4OnURL2.getName());
        assertEquals("browser5.exe", testForBrowser5OnURL2.getName());
    }

    private Document remoteConfiguration1XmlDocument() {
        Configuration configuration = new Configuration(new StubConfigurationSource() {
            public String browserFileNames() {
                return "browser1.exe,browser2.exe";
            }
        });
        return new Document(configuration.asXml(ServerType.STANDARD));
    }

    private Document remoteConfiguration2XmlDocument() {
        Configuration configuration = new Configuration(new StubConfigurationSource() {
            public String browserFileNames() {
                return "browser3.exe,browser4.exe,browser5.exe";
            }
        });
        return new Document(configuration.asXml(ServerType.STANDARD));
    }

    private Document remoteConfiguration3XmlDocument() {
        Configuration configuration = new Configuration(new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://machine4:6060/jsunit,http://machine5:5050/jsunit";
            }
        });
        return new Document(configuration.asXml(ServerType.AGGREGATE));
    }

}
