package net.jsunit;

public class ServerStatusFunctionalTest extends FunctionalTestCase {

    public void testSimple() throws Exception {
        server.logStatus("a message");
        webTester.beginAt("serverstatus");
        assertEquals("a message", webTester.getDialog().getResponseText());
    }

}
