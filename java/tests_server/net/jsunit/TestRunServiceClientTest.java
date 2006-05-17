package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.client.TestRunClient;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.ServerType;
import net.jsunit.model.*;
import net.jsunit.repository.MockUserRepository;

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

            public String remoteMachineURLs() {
                return "http://localhost:1,http://localhost:2,http://localhost:3";
            }
        };
        mockHitter = new MockRemoteServerHitter();

        Configuration configuration1 = new Configuration(new DummyConfigurationSource() {
            public String osString() {
                return PlatformType.WINDOWS.getDisplayName();
            }

            public String browserFileNames() {
                return "iexplore.exe";
            }
        });
        Configuration configuration2 = new Configuration(new DummyConfigurationSource() {
            public String osString() {
                return PlatformType.LINUX.getDisplayName();
            }

            public String browserFileNames() {
                return "firefox.exe";
            }
        });
        Configuration configuration3 = new Configuration(new DummyConfigurationSource() {
            public String osString() {
                return PlatformType.MACINTOSH.getDisplayName();
            }

            public String browserFileNames() {
                return "opera.exe";
            }
        });
        mockHitter.urlToDocument.put("http://localhost:1/jsunit/config", configuration1.asXmlDocument(ServerType.STANDARD));
        mockHitter.urlToDocument.put("http://localhost:2/jsunit/config", configuration2.asXmlDocument(ServerType.STANDARD));
        mockHitter.urlToDocument.put("http://localhost:3/jsunit/config", configuration3.asXmlDocument(ServerType.STANDARD));
        server = new JsUnitAggregateServer(new Configuration(source), mockHitter);
        server.setUserRepository(new MockUserRepository() {
            public User find(String emailAddress, String password) {
                return new User();
            }
        });
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
        TestRunClient client = new TestRunClient("http://localhost:" + port + "/services/TestRunService");
        client.addBrowserSpec(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.INTERNET_EXPLORER));
        client.addBrowserSpec(new BrowserSpecification(PlatformType.LINUX, BrowserType.FIREFOX));
        client.addBrowserSpec(new BrowserSpecification(PlatformType.MACINTOSH, BrowserType.OPERA));
        File page = new File(directory, TEST_PAGE_FILE_NAME);
        Result result = client.send(page);
        assertEquals(3, mockHitter.urlsPassed.size());
        assertMockHitterWasPassedAUrlStartingWith("http://localhost:1/jsunit/runner?url=");
        assertMockHitterWasPassedAUrlStartingWith("http://localhost:2/jsunit/runner?url=");
        assertMockHitterWasPassedAUrlStartingWith("http://localhost:2/jsunit/runner?url=");
    }

    private void assertMockHitterWasPassedAUrlStartingWith(String url) {
        for (String string : mockHitter.urlsPassed) {
            if (string.startsWith(url))
                return;
        }
        fail();
    }

}
