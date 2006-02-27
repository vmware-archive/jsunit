package net.jsunit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.logging.NoOpStatusLogger;
import net.jsunit.model.FarmTestRunResult;
import net.jsunit.utility.XmlUtility;
import org.mortbay.util.MultiException;

import java.net.URL;
import java.net.BindException;
import java.util.List;

public class DistributedTest extends TestCase {

    protected DistributedTestRunManager manager;
    private JsUnitStandardServer server;

  public DistributedTest(ConfigurationSource serverSource, ConfigurationSource farmSource) {
    super(farmSource.remoteMachineURLs());
    server = new JsUnitStandardServer(new Configuration(serverSource));
    manager = new DistributedTestRunManager(new NoOpStatusLogger(), new Configuration(farmSource));
  }

  public void setUp() throws Exception {
      super.setUp();
      startServerIfNecessary();
  }

    private void startServerIfNecessary() throws Exception {
        try {
            server.start();
        } catch (MultiException e) {
            if (!isMultiExceptionJustABindException(e))
                throw e;
            //if a server is already running, fine - we only need it to server content to remote machines
        }
    }

    private boolean isMultiExceptionJustABindException(MultiException e) {
        List exceptions = e.getExceptions();
        return exceptions.size() == 1 && exceptions.get(0) instanceof BindException;
    }

    public void tearDown() throws Exception {
        if (server != null && server.isAlive())
            server.dispose();
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
      FarmTestRunResult result = manager.getFarmTestRunResult();
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
}
