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

    private Configuration remoteServer1Configuration() {
        return new Configuration(new StubConfigurationSource() {
            public String browserFileNames() {
                return "browser0.exe,browser1.exe";
            }
        });
    }

    private Configuration remoteServer2Configuration() {
        return new Configuration(new StubConfigurationSource() {
            public String browserFileNames() {
                return "browser2.exe,browser3.exe";
            }
        });
    }

    protected FunctionalTestAggregateConfigurationSource createConfigurationSource() {
        return new FunctionalTestAggregateConfigurationSource(port);
    }

    protected JsUnitAggregateServer aggregateServer() {
        return (JsUnitAggregateServer) server;
    }

}
