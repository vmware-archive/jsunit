package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.model.Result;
import net.jsunit.utility.XmlUtility;

public class ServerStressTest extends TestCase {

    public void testStressServer() throws Exception {
        PlayRequest request = new PlayRequest();
        for (int i = 0; i < 100; i++) {
            Result result = request.send();
            if (!result.wasSuccessful())
                fail(XmlUtility.asPrettyString(result.asXml()));
        }
    }
}
