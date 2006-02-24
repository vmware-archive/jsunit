package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.logging.StatusLogger;
import net.jsunit.model.FarmTestRunResult;
import net.jsunit.model.TestRunResult;
import net.jsunit.model.TestRunResultBuilder;
import org.jdom.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class DistributedTestRunManager {

    private StatusLogger logger;
    private RemoteRunnerHitter hitter;
    private Configuration configuration;
    private String overrideURL;
    private FarmTestRunResult farmTestRunResult = new FarmTestRunResult();

    public DistributedTestRunManager(StatusLogger logger, Configuration configuration) {
        this(logger, new RemoteMachineRunnerHitter(), configuration);
    }

    public DistributedTestRunManager(StatusLogger logger, RemoteRunnerHitter hitter, Configuration configuration) {
        this(logger, hitter, configuration, null);
    }

    public DistributedTestRunManager(StatusLogger logger, Configuration configuration, String overrideURL) {
        this(logger, new RemoteMachineRunnerHitter(), configuration, overrideURL);
    }

    public DistributedTestRunManager(StatusLogger logger, RemoteRunnerHitter hitter, Configuration configuration, String overrideURL) {
        this.logger = logger;
        this.hitter = hitter;
        this.configuration = configuration;
        this.overrideURL = overrideURL;
    }

    public void runTests() {
        TestRunResultBuilder builder = new TestRunResultBuilder();
        for (URL baseURL : configuration.getRemoteMachineURLs()) {
            TestRunResult testRunResult = null;
            try {
                URL fullURL = buildURL(baseURL);
                Document documentFromRemoteMachine = hitter.hitURL(fullURL);
                testRunResult = builder.build(baseURL, documentFromRemoteMachine);
            } catch (IOException e) {
                if (configuration.shouldIgnoreUnresponsiveRemoteMachines())
                    logger.log("Ignoring unresponsive machine " + baseURL.toString());
                else {
                    testRunResult = new TestRunResult(baseURL);
                    testRunResult.setUnresponsive();
                }
            }
            if (testRunResult != null)
                farmTestRunResult.addTestRunResult(testRunResult);
        }
    }

    private URL buildURL(URL url) throws UnsupportedEncodingException, MalformedURLException {
        String fullURLString = url.toString();
        fullURLString += "/jsunit/runner";
        if (overrideURL != null)
            fullURLString += "?url=" + URLEncoder.encode(overrideURL, "UTF-8");
        else if (configuration.getTestURL() != null)
            fullURLString += "?url=" + URLEncoder.encode(configuration.getTestURL().toString(), "UTF-8");
        return new URL(fullURLString);
    }

    public FarmTestRunResult getFarmTestRunResult() {
        return farmTestRunResult;
    }

    public String getOverrideURL() {
        return overrideURL;
    }
}
