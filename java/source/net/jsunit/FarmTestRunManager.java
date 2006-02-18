package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.TestRunResult;
import net.jsunit.model.TestRunResultBuilder;
import org.jdom.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;

public class FarmTestRunManager {

    private RemoteRunnerHitter hitter;
    private Configuration configuration;
    private String overrideURL;
    private TestRunResult result = new TestRunResult();

    public FarmTestRunManager(Configuration configuration) {
        this(new RemoteMachineRunnerHitter(), configuration);
    }

    public FarmTestRunManager(RemoteRunnerHitter hitter, Configuration configuration) {
        this(hitter, configuration, null);
    }

    public FarmTestRunManager(Configuration configuration, String overrideURL) {
        this(new RemoteMachineRunnerHitter(), configuration, overrideURL);
    }

    public FarmTestRunManager(RemoteRunnerHitter hitter, Configuration configuration, String overrideURL) {
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
                documentFromRemoteMachine = hitter.hitRemoteRunner(fullURL);
                TestRunResult resultFromRemoteMachine = builder.build(documentFromRemoteMachine);
                result.mergeWith(resultFromRemoteMachine);
            } catch (IOException e) {
                result.addCrashedRemoteURL(baseURL);
            }
        }
    }

    private URL buildURL(URL url) throws UnsupportedEncodingException, MalformedURLException {
        String urlString = url.toString();
        String fullURLString = urlString;
        if (!urlString.endsWith("/"))
            fullURLString += "/";
        fullURLString += "jsunit/runner";
        if (overrideURL != null)
            fullURLString += "?url=" + URLEncoder.encode(overrideURL, "UTF-8");
        return new URL(fullURLString);
    }

    public TestRunResult getTestRunResult() {
        return result;
    }

}
