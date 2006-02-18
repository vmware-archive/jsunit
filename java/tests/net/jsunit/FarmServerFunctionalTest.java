package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.ResultType;
import org.jdom.Document;

import java.net.URLEncoder;

public class FarmServerFunctionalTest extends FunctionalTestCase {

    private JsUnitFarmServer farmServer;

    public void setUp() throws Exception {
        super.setUp();
        farmServer = new JsUnitFarmServer(new Configuration(new FunctionalTestFarmConfigurationSource(PORT + 1, PORT)));
        farmServer.start();
    }
    
    protected int webTesterPort() {
    	return PORT + 1;
    }

    public void testHitFarmRunner() throws Exception {
        String url =
        	"/runner?url=" + 
        	URLEncoder.encode("http://localhost:"+PORT+"/jsunit/tests/jsUnitUtilityTests.html", "UTF-8");
		webTester.beginAt(url);
        Document document = responseXmlDocument();
        assertEquals(ResultType.SUCCESS.name(), document.getRootElement().getAttribute("type").getValue());
    }

    public void tearDown() throws Exception {
        farmServer.dispose();
        super.tearDown();
    }

}
