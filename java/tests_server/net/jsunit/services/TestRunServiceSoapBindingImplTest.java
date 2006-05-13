package net.jsunit.services;

import junit.framework.TestCase;
import net.jsunit.AuthenticationException;
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
    private JsUnitAggregateServer aggregateServer;

    protected void setUp() throws Exception {
        super.setUp();
        binding = new TestRunServiceSoapBindingImpl();
        hitter = new MockRemoteServerHitter();
        Configuration aggregateServerConfiguration = new Configuration(new DummyConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:1,http://localhost:2";
            }
        });
        aggregateServer = new JsUnitAggregateServer(aggregateServerConfiguration, hitter);
        aggregateServer.setCachedRemoteConfigurations(Arrays.asList(new RemoteConfiguration[]{
                new RemoteConfiguration(new URL("http://localhost:1/jsunit"), new DummyConfigurationSource()),
                new RemoteConfiguration(new URL("http://localhost:2/jsunit"), new DummyConfigurationSource()),
        }));
        aggregateServer.setUserRepository(new MockUserRepository());
    }

    public void testInit() throws Exception {
        binding.init(createServletEndpointContext("validUsername", "validPassword"));
        assertEquals("validUsername", binding.getUsername());
        assertEquals("validPassword", binding.getPassword());
        assertEquals(aggregateServer, binding.getServer());
    }

    public void testSimple() throws Exception {
        binding.init(createServletEndpointContext("validUsername", "validPassword"));
        DistributedTestRunResult result = binding.runTests(new TestPage());
        assertFalse(result.wasSuccessful());
        assertEquals(2, result._getTestRunResults().size());
    }

    public void testInvalidUsernamePassword() throws Exception {
        binding.init(createServletEndpointContext("invalidUsername", "invalidPassword"));
        try {
            binding.runTests(new TestPage());
            fail();
        } catch (AuthenticationException e) {
        }
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
