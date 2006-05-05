package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.ResultType;

import java.net.URLEncoder;

public class AggregateServerFunctionalTest extends FunctionalTestCase {

    private JsUnitAggregateServer aggregateServer;
    private int otherPort;

    public void setUp() throws Exception {
        otherPort = new TestPortManager().newPort();
        super.setUp();
        aggregateServer = new JsUnitAggregateServer(new Configuration(new FunctionalTestAggregateConfigurationSource(otherPort, port)));
        aggregateServer.start();
    }

    protected int webTesterPort() {
        return otherPort;
    }

    public void testRunFragmentTest() throws Exception {
        webTester.beginAt("/");
        assertOnFragmentRunnerPage();
        webTester.setFormElement("fragment", "assertTrue(true);");
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        assertEquals(ResultType.SUCCESS.name(), responseXmlDocument().getRootElement().getAttribute("type").getValue());
    }

    public void testHitAggregateRunner() throws Exception {
        String url =
                "/runner?url=" + URLEncoder.encode("http://localhost:" + otherPort + "/jsunit/tests/jsUnitUtilityTests.html", "UTF-8");
        webTester.beginAt(url);
        assertEquals(ResultType.SUCCESS.name(), responseXmlDocument().getRootElement().getAttribute("type").getValue());
    }

    public void tearDown() throws Exception {
        aggregateServer.dispose();
        super.tearDown();
    }

}
