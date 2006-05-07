package net.jsunit.server;

import junit.framework.TestCase;
import net.jsunit.InvalidRemoteMachineBrowserCombinationException;
import net.jsunit.RemoteRunSpecification;
import net.jsunit.action.InvalidRemoteMachineUrlBrowserCombination;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.model.Browser;
import net.jsunit.model.RemoteServerConfigurationSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RemoteRunSpecificationBuilderTest extends TestCase {
    private RemoteRunSpecificationBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        builder = new RemoteRunSpecificationBuilder();
    }

    public void testSingleRemoteBrowser() throws Exception {
        String URL = "http://www.example.com";
        Browser browser = new Browser("browser.exe", 3);
        RemoteRunSpecification spec = builder.forSingleRemoteBrowser(
                new URL(URL),
                browser
        );
        assertEquals("http://www.example.com", spec.getRemoteMachineBaseURL().toString());
        List<Browser> remoteBrowsers = spec.getRemoteBrowsers();
        assertEquals(1, remoteBrowsers.size());
        assertEquals(browser, remoteBrowsers.get(0));
    }

    public void testAllBrowsersFromRemoteConfigurations() throws Exception {
        List<RemoteConfiguration> remoteConfigs = someRemoteConfigs();
        List<RemoteRunSpecification> specs = builder.forAllBrowsersFromRemoteConfigurations(remoteConfigs);
        assertEquals(3, specs.size());
        RemoteRunSpecification spec0 = specs.get(0);
        assertEquals("http://www.example.com", spec0.getRemoteMachineBaseURL().toString());
        assertTrue(spec0.isForAllBrowsers());

        RemoteRunSpecification spec1 = specs.get(1);
        assertEquals("http://www.example.net", spec1.getRemoteMachineBaseURL().toString());
        assertTrue(spec1.isForAllBrowsers());

        RemoteRunSpecification spec2 = specs.get(2);
        assertEquals("http://www.example.org", spec2.getRemoteMachineBaseURL().toString());
        assertTrue(spec2.isForAllBrowsers());
    }

    public void testAllBrowsersFromRemoteURLs() throws Exception {
        List<RemoteRunSpecification> specs = builder.forAllBrowsersFromRemoteURLs(
                new URL("http://www.example.com"), new URL("http://www.example.net")
        );
        assertEquals(2, specs.size());
        RemoteRunSpecification spec0 = specs.get(0);
        assertEquals("http://www.example.com", spec0.getRemoteMachineBaseURL().toString());
        assertTrue(spec0.isForAllBrowsers());

        RemoteRunSpecification spec1 = specs.get(1);
        assertEquals("http://www.example.net", spec1.getRemoteMachineBaseURL().toString());
        assertTrue(spec1.isForAllBrowsers());
    }

    public void testIdStringPairs() throws Exception {
        List<RemoteRunSpecification> specs = builder.fromIdStringPairs(
                new String[]{"0_0", "1_0", "1_2"},
                new DummyRemoteServerConfigurationSource(someRemoteConfigs())
        );
        assertEquals(2, specs.size());
        RemoteRunSpecification spec0 = specs.get(0);
        assertEquals("http://www.example.com", spec0.getRemoteMachineBaseURL().toString());
        List<Browser> spec0RemoteBrowsers = spec0.getRemoteBrowsers();
        assertEquals(1, spec0RemoteBrowsers.size());
        assertEquals(new Browser("browser0.exe", 0), spec0RemoteBrowsers.get(0));

        RemoteRunSpecification spec1 = specs.get(1);
        assertEquals("http://www.example.net", spec1.getRemoteMachineBaseURL().toString());
        List<Browser> spec1RemoteBrowsers = spec1.getRemoteBrowsers();
        assertEquals(2, spec1RemoteBrowsers.size());
        assertEquals(new Browser("browser0.exe", 0), spec1RemoteBrowsers.get(0));
        assertEquals(new Browser("browser2.exe", 2), spec1RemoteBrowsers.get(1));
    }

    public void testInvalidIdStringPairs() throws Exception {
        try {
            builder.fromIdStringPairs(
                    new String[]{"0_0", "1_0", "1_4"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidRemoteMachineBrowserCombinationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("1, 4", combo.getDisplayString());
        }

        try {
            builder.fromIdStringPairs(
                    new String[]{"0_0", "3_2", "1_0"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidRemoteMachineBrowserCombinationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("3, 2", combo.getDisplayString());
        }

        try {
            builder.fromIdStringPairs(
                    new String[]{"0_0", "foobar_2", "1_0"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidRemoteMachineBrowserCombinationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("foobar, 2", combo.getDisplayString());
        }

        try {
            builder.fromIdStringPairs(
                    new String[]{"0_0", "5_foobar", "1_0"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidRemoteMachineBrowserCombinationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("5, foobar", combo.getDisplayString());
        }
    }

    public void testMalformedStringIdPairs() throws Exception {
        try {
            builder.fromIdStringPairs(
                    new String[]{"foobar", "1_0", "1_4"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidRemoteMachineBrowserCombinationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("foobar", combo.getDisplayString());
        }
    }

    private List<RemoteConfiguration> someRemoteConfigs() throws MalformedURLException {
        List<RemoteConfiguration> remoteConfigs = new ArrayList<RemoteConfiguration>();
        remoteConfigs.add(new RemoteConfiguration(new URL("http://www.example.com"), new DummyConfigurationSource() {
            public String browserFileNames() {
                return "browser0.exe,browser1.exe";
            }
        }));
        remoteConfigs.add(new RemoteConfiguration(new URL("http://www.example.net"), new DummyConfigurationSource() {
            public String browserFileNames() {
                return "browser0.exe,browser1.exe,browser2.exe";
            }
        }));
        remoteConfigs.add(new RemoteConfiguration(new URL("http://www.example.org"), new DummyConfigurationSource() {
            public String browserFileNames() {
                return "browser0.exe";
            }
        }));
        return remoteConfigs;
    }

    static class DummyRemoteServerConfigurationSource implements RemoteServerConfigurationSource {
        private List<RemoteConfiguration> list;

        public DummyRemoteServerConfigurationSource(List<RemoteConfiguration> list) {
            this.list = list;
        }

        public RemoteConfiguration getRemoteMachineConfigurationById(int id) {
            return list.get(id);
        }

        public List<RemoteConfiguration> getAllRemoteMachineConfigurations() {
            return list;
        }
    }

}
