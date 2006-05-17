package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.logging.BrowserResultRepository;
import net.jsunit.logging.FileBrowserResultRepository;

import java.io.File;

public abstract class StandardServerFunctionalTestCase extends FunctionalTestCase {

    protected static JsUnitStandardServer server;
    private BrowserResultRepository repository;

    protected JsUnitStandardServer createServer() {
        int port = new TestPortManager().newPort();
        Configuration configuration = new Configuration(new FunctionalTestConfigurationSource(port));
        JsUnitStandardServer result = new JsUnitStandardServer(configuration, repository, true);
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
            ServerRegistry.registerStandardServer(server);
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

}
