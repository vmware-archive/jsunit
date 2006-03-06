package net.jsunit;

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
    private JsUnitStandardServer temporaryStandardServer;

    public DistributedTest(ConfigurationSource serverSource, ConfigurationSource farmSource) {
        super(farmSource.remoteMachineURLs());
        temporaryStandardServer = new JsUnitStandardServer(new Configuration(serverSource));
        temporaryStandardServer.setTemporary(true);
        manager = new DistributedTestRunManager(temporaryStandardServer.getLogger(), new Configuration(farmSource));
    }

    public void setUp() throws Exception {
        super.setUp();
        startServerIfNecessary();
    }

    private void startServerIfNecessary() throws Exception {
        try {
            temporaryStandardServer.start();
        } catch (MultiException e) {
            if (!isMultiExceptionASingleBindException(e))
                throw e;
            //if a temporaryStandardServer is already running, fine - we only need it to temporaryStandardServer content to remote machines
        }
    }

    private boolean isMultiExceptionASingleBindException(MultiException e) {
        List exceptions = e.getExceptions();
        return exceptions.size() == 1 && exceptions.get(0) instanceof BindException;
    }

    public void tearDown() throws Exception {
        if (temporaryStandardServer != null && temporaryStandardServer.isAlive())
            temporaryStandardServer.dispose();
        super.tearDown();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
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
        if (!result.wasSuccessful()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(result.displayString());
            buffer.append("\n");
            String xml = XmlUtility.asPrettyString(result.asXml());
            buffer.append(xml);
            fail(buffer.toString());
        }
    }

    public DistributedTestRunManager getDistributedTestRunManager() {
        return manager;
    }

    public JsUnitStandardServer getTemporaryStandardServer() {
        return temporaryStandardServer;
    }
}
