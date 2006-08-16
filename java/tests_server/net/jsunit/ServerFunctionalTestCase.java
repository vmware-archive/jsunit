package net.jsunit;

import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.logging.BrowserResultRepository;
import net.jsunit.logging.FileBrowserResultRepository;

import java.io.File;

public abstract class ServerFunctionalTestCase extends FunctionalTestCase {

    protected static JsUnitServer server;
    private BrowserResultRepository repository;

    protected JsUnitServer createServer() {
        int port = new TestPortManager().newPort();
        ServerConfiguration configuration = new ServerConfiguration(new FunctionalTestConfigurationSource(port));
        JsUnitServer result = new JsUnitServer(configuration, repository);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (server != null)
                    server.dispose();
            }
        });
        return result;
    }

    protected int port() {
        return server.getConfiguration().getPort();
    }

    public void setUp() throws Exception {
        super.setUp();
        repository = createResultRepository();
        if (server == null) {
            server = createServer();
            server.start();
        } else
            JsUnitServer.registerInstance(server);
        server.setResultRepository(repository);
        if (shouldMockOutProcessStarter())
            server.setProcessStarter(new MockProcessStarter());
        else
            server.setProcessStarter(new DefaultProcessStarter());
        createWebTester();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    protected boolean shouldMockOutProcessStarter() {
        return true;
    }

    private BrowserResultRepository createResultRepository() {
        return needsRealResultRepository() ?
                new FileBrowserResultRepository(new File("logs")) :
                new MockBrowserResultRepository();
    }

    protected boolean needsRealResultRepository() {
        return false;
    }

    protected String baseURL() {
        return "http://localhost:" + port() + "/jsunit";
    }
}
