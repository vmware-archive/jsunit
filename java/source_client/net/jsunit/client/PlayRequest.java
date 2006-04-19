package net.jsunit.client;

import net.jsunit.model.TestRunResult;
import net.jsunit.utility.XmlUtility;

import java.io.IOException;

public class PlayRequest {

    public static void main(String[] args) throws IOException {
        TestRunRequest request = new TestRunRequest("http://69.181.237.145/jsunit/runner");
        TestRunResult testRunResult = request.send(null);
        System.out.println(XmlUtility.asPrettyString(testRunResult.asXml()));
    }
}
