package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import net.jsunit.interceptor.RemoteServerHitterInterceptor;

public abstract class AggregateServerFunctionalTestCase extends FunctionalTestCase {

    protected MockRemoteServerHitter mockHitter;

    public void setUp() throws Exception {
        createMockHitter();
        RemoteServerHitterInterceptor.factory = new RemoteServerHitterInterceptor.RemoteServerHitterFactory() {
            public RemoteServerHitter create() {
                return mockHitter;
            }
        };
        super.setUp();
    }

    public void tearDown() throws Exception {
        RemoteServerHitterInterceptor.resetFactory();
        super.tearDown();
    }

    protected JsUnitServer createServer() {
        Configuration configuration = new Configuration(createConfigurationSource());
        return new JsUnitAggregateServer(configuration, mockHitter);
    }

    protected void createMockHitter() {
        mockHitter = new MockRemoteServerHitter();
        mockHitter.urlToDocument.put(
                FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_1 + "/config",
                remoteServer1Configuration().asXmlDocument(ServerType.STANDARD)
        );
        mockHitter.urlToDocument.put(
                FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_2 + "/config",
                remoteServer2Configuration().asXmlDocument(ServerType.STANDARD)
        );
    }

    protected Configuration remoteServer1Configuration() {
        return new Configuration(new StubConfigurationSource() {
            public String osString() {
                return "Windows XP";
            }

            public String browserFileNames() {
                return "iexplore.exe,opera.exe";
            }
        });
    }

    protected Configuration remoteServer2Configuration() {
        return new Configuration(new StubConfigurationSource() {
            public String osString() {
                return "Red Hat Linux SE 4";
            }

            public String browserFileNames() {
                return "firefox.exe,xbrowser.exe";
            }
        });
    }

    protected FunctionalTestAggregateConfigurationSource createConfigurationSource() {
        return new FunctionalTestAggregateConfigurationSource(port);
    }

    protected JsUnitAggregateServer aggregateServer() {
        return (JsUnitAggregateServer) server;
    }


    protected void assertOnLogDisplayerPage() {
        webTester.assertTitleEquals("LogDisplayer - JsUnit");
    }

    protected void assertOnUrlRunnerPage() {
        webTester.assertTitleEquals("URLRunner - JsUnit");
    }

    protected void assertOnUploadRunnerPage() {
        webTester.assertTitleEquals("UploadRunner - JsUnit");
    }

    protected void assertOnFragmentRunnerPage() {
        webTester.assertTitleEquals("FragmentRunner - JsUnit");
    }

    protected void assertOnSignUpPage() {
        webTester.assertTitleEquals("Create account - JsUnit");
    }
}
