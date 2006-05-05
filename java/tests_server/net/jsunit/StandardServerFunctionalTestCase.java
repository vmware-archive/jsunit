package net.jsunit;

import net.jsunit.logging.BrowserResultRepository;
import net.jsunit.logging.FileBrowserResultRepository;

import java.io.File;

public abstract class StandardServerFunctionalTestCase extends FunctionalTestCase {

    protected JsUnitServer createServer() {
        JsUnitStandardServer result = new JsUnitStandardServer(configuration, createResultRepository(), true);
        if (shouldMockOutProcessStarter())
            result.setProcessStarter(new MockProcessStarter());
        return result;
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

    protected JsUnitStandardServer standardServer() {
        return (JsUnitStandardServer) server;
    }
}
