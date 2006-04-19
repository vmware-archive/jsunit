package net.jsunit.client;

import net.jsunit.model.TestRunResult;
import net.jsunit.utility.XmlUtility;

import java.io.IOException;
import java.io.File;

public class PlayRequest {

    public static void main(String[] args) throws IOException {
        TestRunClient client = new TestRunClient("http://69.181.237.145/jsunit/runner");
        TestRunResult testRunResult = client.send(new File("tests"+File.separator+"jsUnitUtilityTests.html"));
        System.out.println(XmlUtility.asPrettyString(testRunResult.asXml()));
    }
}
