package net.jsunit.client;

import net.jsunit.RemoteMachineServerHitter;
import net.jsunit.RemoteServerHitter;
import net.jsunit.model.Result;
import net.jsunit.model.ResultBuilder;
import org.jdom.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunClient {
    private URL serviceURL;
    private RemoteServerHitter hitter;

    public TestRunClient(String serviceURL) {
        this(serviceURL, new RemoteMachineServerHitter());
    }

    public TestRunClient(String serviceURL, RemoteServerHitter hitter) {
        try {
            this.serviceURL = new URL(serviceURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.hitter = hitter;
    }

    public Result send(File testPageFile) throws IOException {
        TestPage testPage = new TestPage(testPageFile);
        Map<String, List<File>> map = new HashMap<String, List<File>>();
        map.put("testPageFile", Arrays.asList(testPage.getTestPageFile()));
        map.put("referencedJsFiles", testPage.getReferencedJsFiles());
        Document document = hitter.postToURL(serviceURL, map);
        ResultBuilder builder = new ResultBuilder();
        return builder.build(document);
    }

}
