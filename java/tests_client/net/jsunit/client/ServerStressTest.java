package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.model.Result;
import net.jsunit.utility.XmlUtility;

public class ServerStressTest extends TestCase {

    public void testStressServer() throws Exception {
        PlayRequest request = new PlayRequest();
        long startTime = System.currentTimeMillis();
        int count = 100;
        for (int i = 0; i < count; i++) {
            System.out.println("Starting run " + i);
            Result result = request.send();
            if (!result.wasSuccessful())
                fail(XmlUtility.asPrettyString(result.asXml()));
        }
        System.out.println("Average time: " + ((System.currentTimeMillis() - startTime) / count / 1000d));
    }
}
