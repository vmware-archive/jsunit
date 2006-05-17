package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import net.jsunit.interceptor.RemoteServerHitterInterceptor;

public abstract class AggregateServerFunctionalTestCase extends FunctionalTestCase {

    protected static JsUnitAggregateServer server;
    protected MockRemoteServerHitter mockHitter;

    public void setUp() throws Exception {
        super.setUp();
        mockHitter = createMockHitter();
        if (server == null)
            createAndStartServer();
        else {
            server.setHitter(mockHitter);
            ServerRegistry.registerAggregateServer(server);
        }
        RemoteServerHitterInterceptor.factory = new RemoteServerHitterInterceptor.RemoteServerHitterFactory() {
            public RemoteServerHitter create() {
                return mockHitter;
            }
        };
        createWebTester();
    }

    private void createAndStartServer() throws Exception {
        if (server == null) {
            Configuration configuration = new Configuration(
                    new FunctionalTestAggregateConfigurationSource(new TestPortManager().newPort())
            );
            server = new JsUnitAggregateServer(configuration, mockHitter);
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    if (server != null)
                        server.dispose();
                }
            });
        }
    }

    public void tearDown() throws Exception {
        RemoteServerHitterInterceptor.resetFactory();
        super.tearDown();
    }

    protected MockRemoteServerHitter createMockHitter() {
        MockRemoteServerHitter mockHitter = new MockRemoteServerHitter();
        mockHitter.urlToDocument.put(
                FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_1 + "/config",
                remoteServer1Configuration().asXmlDocument(ServerType.STANDARD)
        );
        mockHitter.urlToDocument.put(
                FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_2 + "/config",
                remoteServer2Configuration().asXmlDocument(ServerType.STANDARD)
        );
        return mockHitter;
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

    protected void assertOnMyAccountPage() {
        webTester.assertTitleEquals("My account - JsUnit");
    }

    protected int port() {
        return server.getConfiguration().getPort();
    }

}
