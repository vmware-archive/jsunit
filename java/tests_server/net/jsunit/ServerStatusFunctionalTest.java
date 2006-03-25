package net.jsunit;

public class ServerStatusFunctionalTest extends FunctionalTestCase {

    public void testSimple() throws Exception {
        server.logStatus("message 1");
        server.logStatus("message 2");
        server.logStatus("message 3");
        webTester.beginAt("serverstatus");
        String responseText = webTester.getDialog().getResponseText();
        System.out.println("responseText = " + responseText);
        assertTrue(responseText.indexOf("message 1|") != -1);
        assertTrue(responseText.indexOf("message 2|") != -1);
        assertTrue(responseText.endsWith("message 3"));
    }

}
