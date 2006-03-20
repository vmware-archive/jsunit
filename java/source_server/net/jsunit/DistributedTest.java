package net.jsunit;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.utility.XmlUtility;
import org.mortbay.util.MultiException;

import java.net.BindException;
import java.net.URL;
import java.util.List;

public class DistributedTest extends TestCase {

    protected DistributedTestRunManager manager;
    private static JsUnitStandardServer temporaryStandardServer;
    private static Object blocker = new Object();
    private static int serverCount = 0;

    public DistributedTest(ConfigurationSource serverSource, ConfigurationSource farmSource) {
        super(farmSource.remoteMachineURLs());
        ensureTemporaryStandardServerIsCreated(serverSource);
        manager = DistributedTestRunManager.forConfiguration(new Configuration(farmSource));
    }

    private void ensureTemporaryStandardServerIsCreated(ConfigurationSource serverSource) {
        //noinspection SynchronizeOnNonFinalField
        synchronized (blocker) {
            if (temporaryStandardServer == null) {
                temporaryStandardServer = new JsUnitStandardServer(new Configuration(serverSource), true);
            }
        }
    }

    public void setUp() throws Exception {
        super.setUp();
        startServerIfNecessary();
    }

    private void startServerIfNecessary() throws Exception {
        serverCount ++;
        //noinspection SynchronizeOnNonFinalField
        synchronized (blocker) {
            if (!temporaryStandardServer.isAlive()) {
                try {
                    temporaryStandardServer.start();
                } catch (MultiException e) {
                    if (!isMultiExceptionASingleBindException(e))
                        throw e;
                    //if a temporaryStandardServer is already running, fine -
                    //we only need it to serve content to remove machines
                }
            }
        }
    }

    private boolean isMultiExceptionASingleBindException(MultiException e) {
        List exceptions = e.getExceptions();
        return exceptions.size() == 1 && exceptions.get(0) instanceof BindException;
    }

    public void tearDown() throws Exception {
        serverCount --;
        if (serverCount == 0) {
            if (temporaryStandardServer != null && temporaryStandardServer.isAlive()) {
                temporaryStandardServer.dispose();
                temporaryStandardServer = null;
            }
        }
        super.tearDown();
    }

    public static Test suite() {
        TestSuite suite = new ActiveTestSuite();
        ConfigurationSource originalSource = Configuration.resolveSource();
        Configuration configuration = new Configuration(originalSource);
        for (final URL remoteMachineURL : configuration.getRemoteMachineURLs())
            suite.addTest(new DistributedTest(
                    originalSource,
                    new DelegatingConfigurationSource(originalSource) {
                        public String remoteMachineURLs() {
                            return remoteMachineURL.toString();
                        }
                    }));
        return suite;
    }

    protected void runTest() throws Throwable {
        manager.runTests();
        DistributedTestRunResult result = manager.getDistributedTestRunResult();
        temporaryStandardServer.logStatus(XmlUtility.asPrettyString(result.asXml()));
        if (!result.wasSuccessful())
            fail(result.displayString());
    }

    public DistributedTestRunManager getDistributedTestRunManager() {
        return manager;
    }

    public JsUnitStandardServer getTemporaryStandardServer() {
        return temporaryStandardServer;
    }
}
