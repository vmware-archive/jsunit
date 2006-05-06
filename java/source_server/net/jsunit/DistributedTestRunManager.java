package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.DistributedTestRunResultBuilder;
import net.jsunit.model.TestRunResult;
import net.jsunit.model.TestRunResultBuilder;
import org.jdom.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DistributedTestRunManager {

    private Logger logger = Logger.getLogger("net.jsunit");
    private RemoteServerHitter hitter;
    private String overrideURL;
    private Configuration localConfiguration;
    private List<RemoteRunSpecification> remoteRunSpecs;
    private DistributedTestRunResult distributedTestRunResult = new DistributedTestRunResult();

    public DistributedTestRunManager(
            RemoteServerHitter hitter, Configuration localConfiguration, String overrideURL, List<RemoteRunSpecification> specs) {
        this.localConfiguration = localConfiguration;
        this.hitter = hitter;
        this.overrideURL = overrideURL;
        this.remoteRunSpecs = specs;
    }

    public void runTests() {
        List<Thread> threads = new ArrayList<Thread>();
        for (final RemoteRunSpecification spec : remoteRunSpecs)
            threads.add(new Thread("Running JsUnit tests on " + spec.getRemoteMachineBaseURL()) {
                public void run() {
                    runTestsOnRemoteMachine(spec);
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

    private void runTestsOnRemoteMachine(RemoteRunSpecification spec) {
        List<TestRunResult> results = new ArrayList<TestRunResult>();
        URL baseURL = spec.getRemoteMachineBaseURL();
        String baseURLString = baseURL.toString();
        try {
            URL fullURL = spec.buildFullURL(localConfiguration, overrideURL);
            logger.info("Requesting run on remote machine " + spec.getDisplayString());
            Document responseDocument = hitter.hitURL(fullURL);
            logger.info("Received response from remote machine URL " + baseURLString);
            if (responseDocument == null)
                throw new IOException("null response received from remote machine URL " + baseURLString);
            addResultsTo(responseDocument, results);
        } catch (IOException e) {
            if (localConfiguration.shouldIgnoreUnresponsiveRemoteMachines())
                logger.info("Ignoring unresponsive machine URL: " + baseURLString);
            else {
                logger.info("Remote machine URL is unresponsive: " + baseURLString);
                addUnresponsiveResultTo(baseURL, results);
            }
        }
        for (TestRunResult result : results) {
            result.setURL(baseURL);
            //noinspection SynchronizeOnNonFinalField
            synchronized (distributedTestRunResult) {
                distributedTestRunResult.addTestRunResult(result);
            }
        }
    }

    private void addUnresponsiveResultTo(URL baseURL, List<TestRunResult> results) {
        TestRunResult unresponsiveResult = new TestRunResult(baseURL);
        unresponsiveResult.setUnresponsive();
        results.add(unresponsiveResult);
    }

    private void addResultsTo(Document responseDocument, List<TestRunResult> results) {
        if (isMultipleTestRunResultsResult(responseDocument)) {
            DistributedTestRunResult multiple = new DistributedTestRunResultBuilder().build(responseDocument);
            results.addAll(multiple.getTestRunResults());
        } else {
            TestRunResult single = new TestRunResultBuilder().build(responseDocument);
            results.add(single);
        }
    }

    private boolean isMultipleTestRunResultsResult(Document document) {
        return document.getRootElement().getName().equals(DistributedTestRunResult.NAME);
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

    public List<RemoteRunSpecification> getRemoteRunSpecs() {
        return remoteRunSpecs;
    }

}
