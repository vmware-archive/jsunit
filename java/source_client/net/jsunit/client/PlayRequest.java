package net.jsunit.client;

import net.jsunit.model.Result;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.io.IOException;

public class PlayRequest {
    private TestRunClient client;

    public PlayRequest() {
        client = new TestRunClient("http://69.181.237.145/jsunit/runner");
    }

    public static void main(String[] args) throws IOException {
        Result result = new PlayRequest().send();
        System.out.println(XmlUtility.asPrettyString(result.asXml()));
    }

    public Result send() throws IOException {
        return client.send(new File("tests" + File.separator + "jsUnitUtilityTests.html"));
    }
}
