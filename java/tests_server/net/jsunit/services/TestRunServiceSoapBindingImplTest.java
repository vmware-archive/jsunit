package net.jsunit.services;

import junit.framework.TestCase;
import net.jsunit.model.*;
import net.jsunit.utility.XmlUtility;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.MockRemoteServerHitter;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.RemoteConfiguration;

import java.util.Arrays;
import java.net.URL;

public class TestRunServiceSoapBindingImplTest extends TestCase {
    private TestRunServiceSoapBindingImpl binding;
    private MockRemoteServerHitter hitter;

    protected void setUp() throws Exception {
        super.setUp();
        binding = new TestRunServiceSoapBindingImpl();
        hitter = new MockRemoteServerHitter();
        Configuration aggregateServerConfiguration = new Configuration(new DummyConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:1,http://localhost:2";
            }
        });
        JsUnitAggregateServer aggregateServer = new JsUnitAggregateServer(aggregateServerConfiguration, hitter);
        aggregateServer.setCachedRemoteConfigurations(Arrays.asList(new RemoteConfiguration[]{
                new RemoteConfiguration(new URL("http://localhost:1/jsunit"), new DummyConfigurationSource()),
                new RemoteConfiguration(new URL("http://localhost:2/jsunit"), new DummyConfigurationSource()),
        }));
        binding.setAggregateServer(aggregateServer);
    }

    public void testSimple() throws Exception {
        TestPage mockPage = new TestPage();
        mockPage.setContents("<html></html>");
        ServiceResult serviceResult = binding.runTests(mockPage);
        String xml = serviceResult.getXml();
        ResultBuilder builder = new ResultBuilder();
        DistributedTestRunResult result = (DistributedTestRunResult) builder.build(XmlUtility.asXmlDocument(xml));
        assertTrue(result.wasSuccessful());
        assertEquals(2, result.getTestRunResults().size());
    }
}
