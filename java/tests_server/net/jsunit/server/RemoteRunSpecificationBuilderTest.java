package net.jsunit.server;

import junit.framework.TestCase;
import net.jsunit.InvalidBrowserSpecificationException;
import net.jsunit.RemoteRunSpecification;
import net.jsunit.action.InvalidRemoteMachineUrlBrowserCombination;
import net.jsunit.configuration.DummyRemoteConfigurationSource;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.model.*;

import java.io.IOException;
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
        List<RemoteRunSpecification> specs = builder.forIdStringPairs(
                new String[]{"0_0", "1_0", "1_2"},
                new DummyRemoteServerConfigurationSource(someRemoteConfigs())
        );
        assertEquals(2, specs.size());
        RemoteRunSpecification spec0 = specs.get(0);
        assertEquals("http://www.example.com", spec0.getRemoteMachineBaseURL().toString());
        List<Browser> spec0RemoteBrowsers = spec0.getRemoteBrowsers();
        assertEquals(1, spec0RemoteBrowsers.size());
        assertEquals(new Browser("firefox.exe", 0), spec0RemoteBrowsers.get(0));

        RemoteRunSpecification spec1 = specs.get(1);
        assertEquals("http://www.example.net", spec1.getRemoteMachineBaseURL().toString());
        List<Browser> spec1RemoteBrowsers = spec1.getRemoteBrowsers();
        assertEquals(2, spec1RemoteBrowsers.size());
        assertEquals(new Browser("iexplore.exe", 0), spec1RemoteBrowsers.get(0));
        assertEquals(new Browser("xbrowser.exe", 2), spec1RemoteBrowsers.get(1));
    }

    public void testInvalidIdStringPairs() throws Exception {
        try {
            builder.forIdStringPairs(
                    new String[]{"0_0", "1_0", "1_4"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidBrowserSpecificationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("1, 4", combo.getDisplayString());
        }

        try {
            builder.forIdStringPairs(
                    new String[]{"0_0", "3_2", "1_0"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidBrowserSpecificationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("3, 2", combo.getDisplayString());
        }

        try {
            builder.forIdStringPairs(
                    new String[]{"0_0", "foobar_2", "1_0"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidBrowserSpecificationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("foobar, 2", combo.getDisplayString());
        }

        try {
            builder.forIdStringPairs(
                    new String[]{"0_0", "5_foobar", "1_0"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidBrowserSpecificationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("5, foobar", combo.getDisplayString());
        }
    }

    public void testMalformedStringIdPairs() throws Exception {
        try {
            builder.forIdStringPairs(
                    new String[]{"foobar", "1_0", "1_4"},
                    new DummyRemoteServerConfigurationSource(someRemoteConfigs())
            );
            fail();
        } catch (InvalidBrowserSpecificationException e) {
            InvalidRemoteMachineUrlBrowserCombination combo = e.createInvalidRemoteRunSpecification();
            assertEquals("foobar", combo.getDisplayString());
        }
    }

    public void testForBrowserSpecs() throws Exception {
        List<BrowserSpecification> browserSpecs = new ArrayList<BrowserSpecification>();
        browserSpecs.add(new BrowserSpecification(PlatformType.LINUX, BrowserType.FIREFOX));
        browserSpecs.add(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.INTERNET_EXPLORER));
        browserSpecs.add(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.OPERA));
        List<RemoteRunSpecification> result = builder.forBrowserSpecifications(browserSpecs, someRemoteConfigs());
        assertEquals(2, result.size());

        RemoteRunSpecification linuxSpec = result.get(0);
        assertEquals("http://www.example.com", linuxSpec.getRemoteMachineBaseURL().toString());
        assertEquals(1, linuxSpec.getRemoteBrowsers().size());
        assertEquals(new Browser("firefox.exe", 0), linuxSpec.getRemoteBrowsers().get(0));

        RemoteRunSpecification windowsSpec = result.get(1);
        assertEquals("http://www.example.net", windowsSpec.getRemoteMachineBaseURL().toString());
        assertEquals(2, windowsSpec.getRemoteBrowsers().size());
        assertEquals(new Browser("iexplore.exe", 0), windowsSpec.getRemoteBrowsers().get(0));
        assertEquals(new Browser("opera9.exe", 1), windowsSpec.getRemoteBrowsers().get(1));
    }

    public void testInvalidBrowserSpecPlatformType() throws Exception {
        List<BrowserSpecification> browserSpecs = new ArrayList<BrowserSpecification>();
        browserSpecs.add(new BrowserSpecification(PlatformType.LINUX, BrowserType.FIREFOX));
        browserSpecs.add(new BrowserSpecification(PlatformType.MACINTOSH, BrowserType.NETSCAPE));
        try {
            builder.forBrowserSpecifications(browserSpecs, someRemoteConfigs());
            fail();
        } catch (InvalidBrowserSpecificationException e) {
        }
    }

    private List<RemoteConfiguration> someRemoteConfigs() throws IOException {
        List<RemoteConfiguration> remoteConfigs = new ArrayList<RemoteConfiguration>();
        remoteConfigs.add(new RemoteConfiguration(new URL("http://www.example.com"), new DummyRemoteConfigurationSource("http://www.example.com") {
            public String browserFileNames() {
                return "firefox.exe,mybrowser.exe";
            }

            public String osString() {
                return PlatformType.LINUX.getDisplayName();
            }
        }));
        remoteConfigs.add(new RemoteConfiguration(new URL("http://www.example.net"), new DummyRemoteConfigurationSource("http://www.example.com") {
            public String browserFileNames() {
                return "iexplore.exe,opera9.exe,xbrowser.exe";
            }

            public String osString() {
                return PlatformType.WINDOWS.getDisplayName();
            }
        }));
        remoteConfigs.add(new RemoteConfiguration(new URL("http://www.example.org"), new DummyRemoteConfigurationSource("http://www.example.com") {
            public String browserFileNames() {
                return "browser0.exe";
            }

            public String osString() {
                return PlatformType.MACINTOSH.getDisplayName();
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
