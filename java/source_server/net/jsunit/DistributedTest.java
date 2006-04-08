package net.jsunit;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.ResultType;
import net.jsunit.model.TestRunResult;
import org.mortbay.util.MultiException;

import java.net.BindException;
import java.util.List;

public class DistributedTest extends TestCase {

    protected DistributedTestRunManager manager;
    private static JsUnitStandardServer temporaryStandardServer;
    private static Object blocker = new Object();
    private static int serverCount = 0;
    private ConfigurationSource localServerSource;
    private ConfigurationSource aggregateSource;
    private Browser remoteBrowser;
    private String overrideURL;

    public DistributedTest(ConfigurationSource localServerSource, ConfigurationSource aggregateSource) {
        super(aggregateSource.remoteMachineURLs().replace('.', '_'));
        this.localServerSource = localServerSource;
        this.aggregateSource = aggregateSource;
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
        RemoteMachineServerHitter serverHitter = new RemoteMachineServerHitter();
        Configuration aggregateConfiguration = new Configuration(aggregateSource);
        Configuration localConfiguration = new Configuration(localServerSource);
        if (remoteBrowser != null)
            manager = DistributedTestRunManager.forSingleRemoteBrowser(serverHitter, localConfiguration, aggregateConfiguration.getRemoteMachineURLs().get(0), overrideURL, remoteBrowser);
        else
            manager = DistributedTestRunManager.forMultipleRemoteMachines(serverHitter, localConfiguration, aggregateConfiguration.getRemoteMachineURLs(), overrideURL);
        ensureTemporaryStandardServerIsCreated(localServerSource);
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
                    //we only need it to serve content to remote machines
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

    public static Test suite(ConfigurationSource source) {
        TestSuite suite = new ActiveTestSuite();
        new DistributedTestSuiteBuilder(source).addTestsTo(suite);
        return suite;
    }

    public static Test suite() {
        return suite(Configuration.resolveSource());
    }

    protected void runTest() throws Throwable {
        manager.runTests();
        DistributedTestRunResult result = manager.getDistributedTestRunResult();
        if (!result.wasSuccessful()) {
            StringBuffer buffer = new StringBuffer();
            result.addErrorStringTo(buffer);
            System.err.println(buffer.toString());
            fail(result.displayString());
        }
    }

    public DistributedTestRunManager getDistributedTestRunManager() {
        return manager;
    }

    public JsUnitStandardServer getTemporaryStandardServer() {
        return temporaryStandardServer;
    }

    public void limitToBrowser(Browser remoteBrowser) {
        this.remoteBrowser = remoteBrowser;
        setName(remoteBrowser.getFileName());
    }

    public DistributedTestRunResult getResult() {
        return manager.getDistributedTestRunResult();
    }

    public ResultType getResultType() {
        return getResult().getResultType();
    }

    public List<TestRunResult> getTestRunResults() {
        return getResult().getTestRunResults();
    }

    public void setOverrideURL(String overrideURL) {
        this.overrideURL = overrideURL;
    }
}
