package net.jsunit.services;

import junit.framework.TestCase;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.MockRemoteServerHitter;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.TestPage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.server.ServletEndpointContext;
import java.net.URL;
import java.security.Principal;
import java.util.Arrays;

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
        DistributedTestRunResult result = binding.runTests(mockPage);
        assertFalse(result.wasSuccessful());
        assertEquals(2, result._getTestRunResults().size());
    }

    public void testUsernamePassword() throws Exception {
        binding.init(createServletEndpointContext("a username", "a password"));
        assertEquals("a username", binding.getUsername());
        assertEquals("a password", binding.getPassword());
    }

    private ServletEndpointContext createServletEndpointContext(final String username, final String password) {
        return new ServletEndpointContext() {
            public MessageContext getMessageContext() {
                return new org.apache.axis.MessageContext(null) {
                    public String getPassword() {
                        return password;
                    }

                    public String getUsername() {
                        return username;
                    }
                };
            }

            public Principal getUserPrincipal() {
                return null;
            }

            public HttpSession getHttpSession() {
                return null;
            }

            public ServletContext getServletContext() {
                return null;
            }

            public boolean isUserInRole(String s) {
                return false;
            }
        };
    }
}
