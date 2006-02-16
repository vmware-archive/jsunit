package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.ResultType;
import org.jdom.Document;

import java.net.URLEncoder;

public class FarmServerFunctionalTest extends FunctionalTestCase {

    private JsUnitServer server1;
    private JsUnitServer server2;
    private JsUnitFarmServer farmServer;

    public void setUp() throws Exception {
        super.setUp();
        server1 = new JsUnitServer(new Configuration(new FunctionalTestConfigurationSource(PORT + 1)));
        server1.start();
        farmServer = new JsUnitFarmServer(new Configuration(new FunctionalTestFarmConfigurationSource(PORT + 1)));
        farmServer.start();
    }

    public void testHitRunner() throws Exception {
        String url =
        	"/runner?url=" + 
        	URLEncoder.encode("http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html", "UTF-8");
		webTester.beginAt(url);
        Document document = responseXmlDocument();
        assertEquals(ResultType.SUCCESS.name(), document.getRootElement().getAttribute("resultType").getValue());
    }

    public void tearDown() throws Exception {
        server1.dispose();
        server2.dispose();
        farmServer.dispose();
        super.tearDown();
    }

}
