package net.jsunit.client;

import net.jsunit.RemoteMachineServerHitter;
import net.jsunit.RemoteServerHitter;
import net.jsunit.model.TestPage;
import net.jsunit.model.TestRunResult;
import net.jsunit.model.TestRunResultBuilder;
import org.jdom.Document;

import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class TestRunRequest {
    private URL serviceURL;
    private RemoteServerHitter hitter;

    public TestRunRequest(String serviceURL) {
        this(serviceURL, new RemoteMachineServerHitter());
    }

    public TestRunRequest(String serviceURL, RemoteServerHitter hitter) {
        try {
            this.serviceURL = new URL(serviceURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.hitter = hitter;
    }

    public TestRunResult send(TestPage page) throws IOException {
        Map<String, List<File>> map = new HashMap<String, List<File>>();
        map.put("testPageFile", Arrays.asList(new File("tests\\jsUnitAssertionTests.html")));
        Document document = hitter.postToURL(serviceURL, map);
        TestRunResultBuilder builder = new TestRunResultBuilder();
        return builder.build(document);
    }
}
