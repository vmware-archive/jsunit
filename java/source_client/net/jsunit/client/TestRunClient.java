package net.jsunit.client;

import net.jsunit.RemoteMachineServerHitter;
import net.jsunit.RemoteServerHitter;
import net.jsunit.model.Result;
import net.jsunit.model.ResultBuilder;
import net.jsunit.utility.JsUnitURL;
import org.jdom.Document;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunClient {
    private JsUnitURL serviceURL;
    private RemoteServerHitter hitter;
    private String username;
    private String password;

    public TestRunClient(String serviceURL) {
        this(serviceURL, new RemoteMachineServerHitter());
    }

    public TestRunClient(String serviceURL, RemoteServerHitter hitter) {
        this.serviceURL = new JsUnitURL(serviceURL);
        this.hitter = hitter;
    }

    public Result send(File testPageFile) throws IOException {
        TestPage testPage = new TestPage(testPageFile);
        Map<String, List<File>> map = new HashMap<String, List<File>>();
        map.put("testPageFile", Arrays.asList(testPage.getTestPageFile()));
        map.put("referencedJsFiles", testPage.getReferencedJsFiles());
        Document document = hitter.postToURL(serviceURL.asJavaURL(), map);
        ResultBuilder builder = new ResultBuilder();
        return builder.build(document);
    }

    public void setUsername(String username) {
        appendToServiceURL("username", username);
    }

    public void setPassword(String password) {
        appendToServiceURL("password", password);
    }

    private void appendToServiceURL(String key, String value) {
        serviceURL.addParameter(key, value);
    }
}
