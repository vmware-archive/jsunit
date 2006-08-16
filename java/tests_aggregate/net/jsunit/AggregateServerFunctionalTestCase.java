package net.jsunit;

import net.jsunit.configuration.AggregateConfiguration;
import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.configuration.StubConfigurationSource;
import net.jsunit.interceptor.RemoteServerHitterInterceptor;

public abstract class AggregateServerFunctionalTestCase extends FunctionalTestCase {

    protected static JsUnitAggregateServer server;
    private MockRemoteServerHitter mockHitter;

    protected JsUnitAggregateServer createServer() {
        final int port = new TestPortManager().newPort();
        AggregateConfiguration configuration = new AggregateConfiguration(new FunctionalTestConfigurationSource(port));
        JsUnitAggregateServer result = new JsUnitAggregateServer(configuration, mockHitter);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (server != null)
                    server.dispose();
            }
        });
        return result;
    }

    protected MockRemoteServerHitter createMockHitter() {
        MockRemoteServerHitter mockHitter = new MockRemoteServerHitter();
        mockHitter.urlToDocument.put(
                FunctionalTestConfigurationSource.REMOTE_SERVER_URL_1 + "/config",
                remoteServer1Configuration().asXmlDocument()
        );
        mockHitter.urlToDocument.put(
                FunctionalTestConfigurationSource.REMOTE_SERVER_URL_2 + "/config",
                remoteServer2Configuration().asXmlDocument()
        );
        return mockHitter;
    }

    protected ServerConfiguration remoteServer1Configuration() {
        return new AggregateConfiguration(new StubConfigurationSource() {
            public String osString() {
                return "Windows XP";
            }

            public String browserFileNames() {
                return "iexplore.exe,opera.exe";
            }
        });
    }

    protected ServerConfiguration remoteServer2Configuration() {
        return new AggregateConfiguration(new StubConfigurationSource() {
            public String osString() {
                return "Mac OS X";
            }

            public String browserFileNames() {
                return "safari.sh";
            }
        });
    }

    protected int port() {
        return server.getConfiguration().getPort();
    }

    public void setUp() throws Exception {
        super.setUp();
        mockHitter = createMockHitter();
        if (server == null) {
            server = createServer();
            server.start();
        } else {
            JsUnitAggregateServer.registerInstance(server);
            server.setHitter(createMockHitter());
        }
        RemoteServerHitterInterceptor.factory = new RemoteServerHitterInterceptor.RemoteServerHitterFactory() {
            public RemoteServerHitter create() {
                return mockHitter;
            }
        };
        createWebTester();
    }

    protected String baseURL() {
        return "http://localhost:" + port() + "/jsunit";
    }

}
