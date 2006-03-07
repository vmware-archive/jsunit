package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.logging.JsUnitLogger;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.TestRunResult;
import net.jsunit.model.TestRunResultBuilder;
import org.jdom.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DistributedTestRunManager {

    private JsUnitLogger logger;
    private RemoteRunnerHitter hitter;
    private Configuration configuration;
    private String overrideURL;
    private DistributedTestRunResult distributedTestRunResult = new DistributedTestRunResult();

    public DistributedTestRunManager(JsUnitLogger logger, Configuration configuration) {
        this(logger, new RemoteMachineRunnerHitter(), configuration);
    }

    public DistributedTestRunManager(JsUnitLogger logger, RemoteRunnerHitter hitter, Configuration configuration) {
        this(logger, hitter, configuration, null);
    }

    public DistributedTestRunManager(JsUnitLogger logger, RemoteRunnerHitter hitter, Configuration configuration, String overrideURL) {
        this.logger = logger;
        this.hitter = hitter;
        this.configuration = configuration;
        this.overrideURL = overrideURL;
    }

    public void runTests() {
        List<Thread> threads = new ArrayList<Thread>();
        for (final URL baseURL : configuration.getRemoteMachineURLs())
            threads.add(new Thread("Run JsUnit tests on " + baseURL) {
                public void run() {
                    runTestsOnRemoteMachine(baseURL);
                }
            });
        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("One of the test threads was interrupted.");
            }
        }
    }

    private void runTestsOnRemoteMachine(URL baseURL) {
        TestRunResult testRunResult = null;
        try {
            URL fullURL = buildURL(baseURL);
            logger.log("Requesting run on remote machine URL " + baseURL, true);
            Document documentFromRemoteMachine = hitter.hitURL(fullURL);
            logger.log("Received response from remove machine URL " + baseURL, true);
            testRunResult = new TestRunResultBuilder().build(documentFromRemoteMachine);
        } catch (IOException e) {
            if (configuration.shouldIgnoreUnresponsiveRemoteMachines())
                logger.log("Ignoring unresponsive machine " + baseURL.toString(), true);
            else {
                logger.log("Remote machine URL is unresponsive: " + baseURL.toString(), true);
                testRunResult = new TestRunResult(baseURL);
                testRunResult.setUnresponsive();
            }
        }
        if (testRunResult != null) {
            testRunResult.setURL(baseURL);
            synchronized(distributedTestRunResult) {
                distributedTestRunResult.addTestRunResult(testRunResult);
            }
        }
    }

    private URL buildURL(URL url) throws UnsupportedEncodingException, MalformedURLException {
        String fullURLString = url.toString();
        fullURLString += "/runner";
        if (overrideURL != null)
            fullURLString += "?url=" + URLEncoder.encode(overrideURL, "UTF-8");
        else if (configuration.getTestURL() != null)
            fullURLString += "?url=" + URLEncoder.encode(configuration.getTestURL().toString(), "UTF-8");
        return new URL(fullURLString);
    }

    public DistributedTestRunResult getDistributedTestRunResult() {
        return distributedTestRunResult;
    }

    public String getOverrideURL() {
        return overrideURL;
    }

  public void setOverrideURL(String overrideURL) {
    this.overrideURL = overrideURL;
  }
}
