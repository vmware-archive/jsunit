package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.logging.StatusLogger;
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
    private TestRunResult result = new TestRunResult();

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
            Document documentFromRemoteMachine;
            try {
                URL fullURL = buildURL(baseURL);
                documentFromRemoteMachine = hitter.hitURL(fullURL);
                TestRunResult resultFromRemoteMachine = builder.build(documentFromRemoteMachine);
                result.mergeWith(resultFromRemoteMachine);
            } catch (IOException e) {
                if (configuration.shouldIgnoreUnresponsiveRemoteMachines())
                    logger.log("Ignoring unresponsive machine " + baseURL.toString());
                else
                    result.addCrashedRemoteURL(baseURL);
            }
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

    public TestRunResult getTestRunResult() {
        return result;
    }

    public String getOverrideURL() {
        return overrideURL;
    }
}
