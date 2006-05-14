package net.jsunit.services;

import junit.framework.TestCase;
import net.jsunit.*;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.model.BrowserSpecification;
import net.jsunit.model.BrowserType;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.TestPage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.server.ServletEndpointContext;
import java.net.URL;
import java.security.Principal;
import java.util.Arrays;
import java.rmi.RemoteException;

public class TestRunServiceSoapBindingImplTest extends TestCase {
    private TestRunServiceSoapBindingImpl binding;
    private JsUnitAggregateServer aggregateServer;
    private MockRemoteServerHitter hitter;

    protected void setUp() throws Exception {
        super.setUp();
        binding = new TestRunServiceSoapBindingImpl();
        Configuration aggregateServerConfiguration = new Configuration(new DummyConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:1,http://localhost:2";
            }
        });
        hitter = new MockRemoteServerHitter();
        aggregateServer = new JsUnitAggregateServer(aggregateServerConfiguration, hitter);
        aggregateServer.setCachedRemoteConfigurations(Arrays.asList(new RemoteConfiguration[]{
                new RemoteConfiguration(new URL("http://localhost:1/jsunit"), new DummyConfigurationSource() {
                    public String osString() {
                        return PlatformType.WINDOWS.getDisplayName();
                    }

                    public String browserFileNames() {
                        return "iexplore.exe,netscape6.exe";
                    }

                }),
                new RemoteConfiguration(new URL("http://localhost:2/jsunit"), new DummyConfigurationSource() {
                    public String osString() {
                        return PlatformType.LINUX.getDisplayName();
                    }

                    public String browserFileNames() {
                        return "opera9";
                    }
                })
        }));

        aggregateServer.setUserRepository(new MockUserRepository());
    }

    public void testInit() throws Exception {
        binding.init(createServletEndpointContext("validUsername", "validPassword"));
        assertEquals("validUsername", binding.getUsername());
        assertEquals("validPassword", binding.getPassword());
        assertEquals(aggregateServer, binding.getServer());
    }

    public void testRunTests() throws Exception {
        binding.init(createServletEndpointContext("validUsername", "validPassword"));
        BrowserSpecification[] browserSpecs = new BrowserSpecification[]{
                new BrowserSpecification(PlatformType.LINUX, BrowserType.OPERA),
                new BrowserSpecification(PlatformType.WINDOWS, BrowserType.INTERNET_EXPLORER),
                new BrowserSpecification(PlatformType.WINDOWS, BrowserType.NETSCAPE)
        };
        DistributedTestRunResult distributedResult = binding.runTests(new TestPage(), browserSpecs);
        assertFalse(distributedResult.wasSuccessful());
        assertEquals(2, distributedResult._getTestRunResults().size());
        assertEquals(2, hitter.urlsPassed.size());
    }

    public void testRunTestsWithInvalidBrowserSpecification() throws Exception {
        binding.init(createServletEndpointContext("validUsername", "validPassword"));
        BrowserSpecification[] browserSpecs = new BrowserSpecification[]{
                new BrowserSpecification(PlatformType.LINUX, BrowserType.NETSCAPE),
                new BrowserSpecification(PlatformType.WINDOWS, BrowserType.FIREFOX)
        };
        try {
            binding.runTests(new TestPage(), browserSpecs);
            fail();
        } catch (RemoteException e) {
            assertTrue(e.getCause() instanceof InvalidBrowserSpecificationException);
        }
    }

    public void testInvalidUsernamePassword() throws Exception {
        binding.init(createServletEndpointContext("invalidUsername", "invalidPassword"));
        try {
            binding.runTests(new TestPage(), new BrowserSpecification[]{});
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
