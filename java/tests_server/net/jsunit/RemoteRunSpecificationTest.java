package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.model.Browser;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class RemoteRunSpecificationTest extends TestCase {
    private RemoteRunSpecification spec;
    private String overrideURL;

    protected void setUp() throws Exception {
        super.setUp();
        spec = new RemoteRunSpecification(new URL("http://www.example.com"));
        overrideURL = "http://www.example.com/testRunner?testPage=http://www.example.com/myTests.html";
    }

    public void testInitialConditions() throws Exception {
        assertEquals("http://www.example.com", spec.getRemoteMachineBaseURL().toString());
    }

    public void testIsForAllBrowsers() throws Exception {
        assertTrue(spec.isForAllBrowsers());
        spec.addBrowser(new Browser("browser0.exe", 0));
        assertFalse(spec.isForAllBrowsers());
    }

    public void testAddBrowserGetBrowsers() throws Exception {
        Browser browser0 = new Browser("browser0.exe", 0);
        Browser browser1 = new Browser("browser1.exe", 1);
        spec.addBrowser(browser0);
        spec.addBrowser(browser1);
        List<Browser> retrieved = spec.getRemoteBrowsers();
        assertEquals(2, retrieved.size());
        assertEquals(browser0, retrieved.get(0));
        assertEquals(browser1, retrieved.get(1));
    }

    public void testBuildURLNoBrowsers() throws Exception {
        URL url = spec.buildFullURL(new Configuration(new DummyConfigurationSource()), overrideURL);
        assertEquals("http://www.example.com/runner?url=" + URLEncoder.encode(overrideURL, "UTF-8"), url.toString());
    }

    public void testBuildURLWithBrowsers() throws Exception {
        spec.addBrowser(new Browser("browser8.exe", 8));
        spec.addBrowser(new Browser("browser4.exe", 4));
        URL url = spec.buildFullURL(
                new Configuration(new DummyConfigurationSource()),
                overrideURL
        );
        assertEquals(
                "http://www.example.com/runner?url=" + URLEncoder.encode(overrideURL, "UTF-8") + "&browserId=8&browserId=4",
                url.toString()
        );
    }
    
    public void testGetDisplayString() throws Exception {
        assertEquals("http://www.example.com: all browsers", spec.getDisplayString());

        spec.addBrowser(new Browser("browser0.exe;;IE 6.0", 0));
        spec.addBrowser(new Browser("browser1.exe;;Mozilla 1.3", 1));

        assertEquals("http://www.example.com: IE 6.0, Mozilla 1.3", spec.getDisplayString());
    }

}
