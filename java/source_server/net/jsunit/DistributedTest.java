package net.jsunit;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.configuration.CompositeConfigurationSource;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.model.Browser;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.ResultType;
import net.jsunit.model.TestRunResult;
import net.jsunit.server.RemoteRunSpecificationBuilder;
import org.mortbay.util.MultiException;

import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

public class DistributedTest extends TestCase {

    protected DistributedTestRunManager manager;
    private static WebServer temporaryServer;
    private static Object blocker = new Object();
    private static int serverCount = 0;
    private ConfigurationSource source;
    private Browser remoteBrowser;
    private String overrideURL;
    private WebServerFactory serverFactory;

    public DistributedTest(ConfigurationSource source) {
        this(source, new WebServerFactory() {
            public WebServer create(ServerConfiguration configuration) {
                return new JsUnitServer(configuration);
            }
        });
    }

    public DistributedTest(ConfigurationSource source, WebServerFactory serverFactory) {
        super(source.remoteMachineURLs().replace('.', '_'));
        this.source = source;
        this.serverFactory = serverFactory;
    }

    private void ensureTemporaryServerIsCreated() {
        //noinspection SynchronizeOnNonFinalField
        synchronized (blocker) {
            if (temporaryServer == null) {
                temporaryServer = serverFactory.create(new ServerConfiguration(source));
            }
        }
    }

    public void setUp() throws Exception {
        super.setUp();
        RemoteMachineServerHitter serverHitter = new RemoteMachineServerHitter();
        ServerConfiguration configuration = new ServerConfiguration(source);
        List<RemoteRunSpecification> specs = new ArrayList<RemoteRunSpecification>();
        RemoteRunSpecificationBuilder builder = new RemoteRunSpecificationBuilder();
        if (remoteBrowser != null)
            specs.add(builder.forSingleRemoteBrowser(configuration.getRemoteMachineURLs().get(0), remoteBrowser));
        else
            specs.addAll(builder.forAllBrowsersFromRemoteURLs(configuration.getRemoteMachineURLs()));
        manager = new DistributedTestRunManager(serverHitter, configuration, overrideURL, specs);
        manager.setListener(new DistributedTestRunListenerImpl(configuration));
        ensureTemporaryServerIsCreated();
        startServerIfNecessary();
    }

    private void startServerIfNecessary() throws Exception {
        serverCount++;
        //noinspection SynchronizeOnNonFinalField
        synchronized (blocker) {
            if (!temporaryServer.isAlive()) {
                try {
                    temporaryServer.start();
                } catch (MultiException e) {
                    if (!isMultiExceptionASingleBindException(e))
                        throw e;
                    //if a temporaryServer is already running, fine -
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
        serverCount--;
        if (serverCount == 0) {
            if (temporaryServer != null && temporaryServer.isAlive()) {
                temporaryServer.dispose();
                temporaryServer = null;
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
        return suite(CompositeConfigurationSource.resolve());
    }

    protected void runTest() throws Throwable {
        manager.runTests();
        DistributedTestRunResult result = manager.getDistributedTestRunResult();
        if (!result.wasSuccessful()) {
            StringBuffer buffer = new StringBuffer();
            result.addErrorStringTo(buffer);
            fail(result.displayString() + "\n" + buffer.toString() + "\n");
        }
    }

    public DistributedTestRunManager getDistributedTestRunManager() {
        return manager;
    }

    public WebServer getTemporaryServer() {
        return temporaryServer;
    }

    public void limitToBrowser(Browser remoteBrowser) {
        this.remoteBrowser = remoteBrowser;
        setName(remoteBrowser.getDisplayName());
    }

    public DistributedTestRunResult getResult() {
        return manager.getDistributedTestRunResult();
    }

    public ResultType getResultType() {
        return getResult()._getResultType();
    }

    public List<TestRunResult> getTestRunResults() {
        return getResult()._getTestRunResults();
    }

    public void setOverrideURL(String overrideURL) {
        this.overrideURL = overrideURL;
    }
}
