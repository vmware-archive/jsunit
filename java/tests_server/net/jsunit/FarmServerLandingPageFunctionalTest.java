package net.jsunit;

import com.meterware.httpunit.HttpUnitOptions;
import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.sourceforge.jwebunit.WebTester;

public class FarmServerLandingPageFunctionalTest extends TestCase {

    static {
        HttpUnitOptions.setScriptingEnabled(false);
    }

    private WebTester webTester;
    private JsUnitFarmServer server;

    protected void setUp() throws Exception {
        super.setUp();
        Configuration configuration = new Configuration(new FunctionalTestConfigurationSource(7000));
        server = new JsUnitFarmServer(configuration);
        server.start();
        webTester = new WebTester();
        webTester.getTestContext().setBaseUrl("http://localhost:7000/jsunit");
    }

    protected void tearDown() throws Exception {
        server.dispose();
        super.tearDown();
    }

    public void testSimple() throws Exception {
        webTester.beginAt("/");
        assertOnLandingPage();
        webTester.assertLinkPresentWithText("runner");
        webTester.assertLinkNotPresentWithText("displayer");
        webTester.assertLinkPresentWithText("testRunner.html");
        webTester.assertLinkPresentWithText("config");
    }

    private void assertOnLandingPage() {
        webTester.assertTitleEquals("JsUnit  Farm Server");
    }

}
