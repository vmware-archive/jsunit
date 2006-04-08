package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.ResultType;
import org.jdom.Document;

import java.net.URLEncoder;

public class AggregateServerFunctionalTest extends FunctionalTestCase {

    private JsUnitAggregateServer aggregateServer;
    private int otherPort;

    public void setUp() throws Exception {
        super.setUp();
        otherPort = new TestPortManager().newPort();
        aggregateServer = new JsUnitAggregateServer(new Configuration(new FunctionalTestAggregateConfigurationSource(otherPort, port)));
        aggregateServer.start();
    }

    protected int webTesterPort() {
        return otherPort;
    }

    public void testHitAggregateRunner() throws Exception {
        String url =
                "/runner?url=" + URLEncoder.encode("http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html", "UTF-8");
        webTester.beginAt(url);
        Document document = responseXmlDocument();
        assertEquals(ResultType.SUCCESS.name(), document.getRootElement().getAttribute("type").getValue());
    }

    public void tearDown() throws Exception {
        aggregateServer.dispose();
        super.tearDown();
    }

}
