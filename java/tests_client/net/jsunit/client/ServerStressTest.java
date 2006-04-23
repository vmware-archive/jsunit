package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.model.Result;
import net.jsunit.utility.XmlUtility;

import java.io.File;

public class ServerStressTest extends TestCase {

    //average all machines 6.12
    //average maverick: 3.07
    //average iceman: 5.71
    //average slider: 6.17

    public void testStressServer() throws Exception {
        TestRunClient client = new TestRunClient("http://69.181.237.145/jsunit/runner");
        long startTime = System.currentTimeMillis();
        int count = 50;
        for (int i = 0; i < count; i++) {
            System.out.println("Starting run " + i);
            Result result = client.send(new File("tests", "jsUnitUtilityTests.html"));
            if (!result.wasSuccessful())
                fail(XmlUtility.asPrettyString(result.asXml()));
        }
        System.out.println("Average time: " + ((System.currentTimeMillis() - startTime) / count / 1000d));
    }
}
