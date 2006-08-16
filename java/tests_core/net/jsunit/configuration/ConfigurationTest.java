package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserSpecification;
import net.jsunit.model.BrowserType;
import net.jsunit.model.PlatformType;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationTest extends TestCase {

    public void testFull() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new FullValidConfigurationSource());
        List<Browser> expectedBrowsers = new ArrayList<Browser>();
        expectedBrowsers.add(new Browser("iexplore.exe", 0));
        expectedBrowsers.add(new Browser("opera.exe", 1));
        assertEquals(expectedBrowsers, configuration.getBrowsers());
        assertEquals(new File("logs" + File.separator + "directory"), configuration.getLogsDirectory());
        assertEquals(1234, configuration.getPort());
        assertEquals(new File("resource" + File.separator + "base"), configuration.getResourceBase());
        assertEquals("http://www.example.com:1234/", configuration.getTestURL().toString());
        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
        assertEquals(76, configuration.getTimeoutSeconds());
        List<URL> expectedRemoteMachineURLs = new ArrayList<URL>();
        expectedRemoteMachineURLs.add(new URL("http://127.0.0.1:8082/jsunit"));
        expectedRemoteMachineURLs.add(new URL("http://localhost:8081/jsunit"));
        assertEquals(expectedRemoteMachineURLs, configuration.getRemoteMachineURLs());
        assertTrue(configuration.shouldIgnoreUnresponsiveRemoteMachines());
    }

    public void testMinimal() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new MinimalValidConfigurationSource());
        assertEquals(new File("."), configuration.getResourceBase());
        assertEquals(new File("logs"), configuration.getLogsDirectory());
        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
        assertEquals(60, configuration.getTimeoutSeconds());
        assertFalse(configuration.shouldIgnoreUnresponsiveRemoteMachines());
    }

    public void testBadRemoteMachineURLs() throws Exception {
        try {
            new ServerConfiguration(new StubConfigurationSource() {
                public String remoteMachineURLs() {
                    return "invalid url";
                }
            });
            fail();
        } catch (ConfigurationException e) {
        }
    }

    public void testBadURL() throws Exception {
        try {
            new ServerConfiguration(new StubConfigurationSource() {
                public String url() {
                    return "invalid url";
                }
            });
            fail();
        } catch (ConfigurationException e) {
        }
    }

    public void testBadPort() throws Exception {
        try {
            new ServerConfiguration(new StubConfigurationSource() {
                public String port() {
                    return "invalid number";
                }
            });
            fail();
        } catch (ConfigurationException e) {
        }
    }

    public void testBadTimeoutSeconds() throws Exception {
        try {
            new ServerConfiguration(new StubConfigurationSource() {
                public String timeoutSeconds() {
                    return "invalid number";
                }
            });
            fail();
        } catch (ConfigurationException e) {
        }
    }

    public void testAsXmlForConfiguration() throws Exception {
        FullValidConfigurationSource source = new FullValidConfigurationSource();
        ServerConfiguration configuration = new ServerConfiguration(source);
        File logsDirectory = new File(source.logsDirectory());
        File resourceBase = new File(source.resourceBase());
        String expectedXML = "<configuration type=\"" + ServerType.SERVER.name() + "\">" +
                "<os>386 - Windows XP</os>" +
                "<ipAddress>" + FullValidConfigurationSource.IP_ADDRESS + "</ipAddress>" +
                "<hostname>" + FullValidConfigurationSource.HOSTNAME + "</hostname>" +
                "<browserFileNames>" +
                "<browserFileName id=\"0\">iexplore.exe</browserFileName>" +
                "<browserFileName id=\"1\">opera.exe</browserFileName>" +
                "</browserFileNames>" +
                "<closeBrowsersAfterTestRuns>true</closeBrowsersAfterTestRuns>" +
                "<description>This is the best server ever</description>" +
                "<ignoreUnresponsiveRemoteMachines>true</ignoreUnresponsiveRemoteMachines>" +
                "<logsDirectory>" + logsDirectory.getAbsolutePath() + "</logsDirectory>" +
                "<port>1234</port>" +
                "<remoteMachineURLs>" +
                "<remoteMachineURL id=\"0\">http://127.0.0.1:8082/jsunit</remoteMachineURL>" +
                "<remoteMachineURL id=\"1\">http://localhost:8081/jsunit</remoteMachineURL>" +
                "</remoteMachineURLs>" +
                "<resourceBase>" + resourceBase.getAbsolutePath() + "</resourceBase>" +
                "<timeoutSeconds>76</timeoutSeconds>" +
                "<url>http://www.example.com:1234/</url>" +
                "</configuration>";
        assertEquals(expectedXML, XmlUtility.asString(configuration.asXml()));
    }

    public void testGetBrowserById() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new FullValidConfigurationSource());
        assertEquals(new Browser("iexplore.exe", 0), configuration.getBrowserById(0));
        assertEquals(new Browser("opera.exe", 1), configuration.getBrowserById(1));
        assertNull(configuration.getBrowserById(900));
    }

    public void testAsArgumentsArray() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new FullValidConfigurationSource());
        String[] arguments = configuration.asArgumentsArray();

        int index = 0;

        assertEquals("-browserFileNames", arguments[index++]);
        assertEquals("iexplore.exe,opera.exe", arguments[index++]);

        assertEquals("-closeBrowsersAfterTestRuns", arguments[index++]);
        assertEquals("true", arguments[index++]);

        assertEquals("-description", arguments[index++]);
        assertEquals("This is the best server ever", arguments[index++]);

        assertEquals("-ignoreUnresponsiveRemoteMachines", arguments[index++]);
        assertEquals("true", arguments[index++]);

        assertEquals("-logsDirectory", arguments[index++]);
        assertEquals(new File("logs" + File.separator + "directory").getAbsolutePath(), arguments[index++]);

        assertEquals("-port", arguments[index++]);
        assertEquals("1234", arguments[index++]);

        assertEquals("-remoteMachineURLs", arguments[index++]);
        assertEquals("http://127.0.0.1:8082/jsunit,http://localhost:8081/jsunit", arguments[index++]);

        assertEquals("-resourceBase", arguments[index++]);
        assertEquals(new File("resource/base").getAbsolutePath(), arguments[index++]);

        assertEquals("-timeoutSeconds", arguments[index++]);
        assertEquals("76", arguments[index++]);

        assertEquals("-url", arguments[index++]);
        assertEquals("http://www.example.com:1234/", arguments[index]);

        assertEquals(index + 1, arguments.length);
    }

    public void testDuplicateBrowserFileNamesAndRemoteMachineURLs() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new DuplicatesConfigurationSource());
        List<Browser> browsers = configuration.getBrowsers();
        assertEquals(3, browsers.size());
        assertEquals(new Browser("xbrowser.exe", 2), browsers.get(2));
        assertEquals(new Browser("iexplore.exe", 0), browsers.get(0));
        assertEquals(new Browser("opera.exe", 1), browsers.get(1));

        List<URL> remoteMachineURLs = configuration.getRemoteMachineURLs();
        assertEquals(4, remoteMachineURLs.size());
        assertEquals("http://machine1:8080/jsunit", remoteMachineURLs.get(0).toString());
        assertEquals("http://machine1:8081/jsunit", remoteMachineURLs.get(1).toString());
        assertEquals("http://machine2:9090/jsunit", remoteMachineURLs.get(2).toString());
        assertEquals("http://machine3:9090/jsunit", remoteMachineURLs.get(3).toString());
    }

    public void testEquals() throws Exception {
        ServerConfiguration configuration1 = new ServerConfiguration(new FullValidConfigurationSource());
        ServerConfiguration configuration2 = new ServerConfiguration(new FullValidConfigurationSource());
        ServerConfiguration configuration3 = new ServerConfiguration(new MinimalValidConfigurationSource());
        assertTrue(configuration1.equals(configuration1));
        assertTrue(configuration1.equals(configuration2));
        assertFalse(configuration1.equals(configuration3));
    }

    public void testBrowserOrdering() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new DummyConfigurationSource() {
            public String browserFileNames() {
                return "browserC.exe,browserA.exe,browserB.exe";
            }
        });
        List<Browser> browsers = configuration.getBrowsers();
        assertEquals(new Browser("browserA.exe", 0), browsers.get(0));
        assertEquals(new Browser("browserB.exe", 1), browsers.get(1));
        assertEquals(new Browser("browserC.exe", 2), browsers.get(2));
    }

    public void testRemoteServerOrdering() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new DummyConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://www.exampleC.com,http://www.exampleA.com,http://www.exampleB.com";
            }

        });
        List<URL> remoteMachineURLs = configuration.getRemoteMachineURLs();
        assertEquals("http://www.exampleA.com/jsunit", remoteMachineURLs.get(0).toString());
        assertEquals("http://www.exampleB.com/jsunit", remoteMachineURLs.get(1).toString());
        assertEquals("http://www.exampleC.com/jsunit", remoteMachineURLs.get(2).toString());
    }

    public void testHasPlatformType() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new FullValidConfigurationSource());
        assertTrue(configuration.hasPlatformType(PlatformType.WINDOWS));
        assertFalse(configuration.hasPlatformType(PlatformType.LINUX));
    }

    public void testGetBrowserOfTypeAndVersion() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new FullValidConfigurationSource());
        assertEquals(new Browser("iexplore.exe", 0), configuration.getBrowserMatching(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.INTERNET_EXPLORER)));
        assertEquals(new Browser("opera.exe", 1), configuration.getBrowserMatching(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.OPERA)));
        assertNull(configuration.getBrowserMatching(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.FIREFOX)));
    }

    static class FullValidConfigurationSource implements ConfigurationSource {
        public static final String IP_ADDRESS = "123.45.67.890";
        public static final String HOSTNAME = "services.jsunit.net";
        public static final String DUMMY_SECRET_KEY = "1234567890123456";

        public String resourceBase() {
            return "resource" + File.separator + "base";
        }

        public String port() {
            return "1234";
        }

        public String logsDirectory() {
            return "logs" + File.separator + "directory";
        }

        public String browserFileNames() {
            return "iexplore.exe,opera.exe";
        }

        public String url() {
            return "http://www.example.com:1234/";
        }

        public String ignoreUnresponsiveRemoteMachines() {
            return "true";
        }

        public String osString() {
            return "386 - Windows XP";
        }

        public String ipAddress() {
            return IP_ADDRESS;
        }

        public String hostname() {
            return HOSTNAME;
        }

        public String closeBrowsersAfterTestRuns() {
            return "true";
        }

        public String description() {
            return "This is the best server ever";
        }

        public String timeoutSeconds() {
            return "76";
        }

        public String remoteMachineURLs() {
            return "http://localhost:8081,http://127.0.0.1:8082";
        }
    }

    static class MinimalValidConfigurationSource extends StubConfigurationSource {
        public String remoteMachineURLs() {
            return "http://localhost:8081,http://127.0.0.1:8082";
        }

    }

    static class DuplicatesConfigurationSource extends StubConfigurationSource {
        public String browserFileNames() {
            return "iexplore.exe,opera.exe,iexplore.exe,iexplore.exe,xbrowser.exe";
        }

        public String remoteMachineURLs() {
            return "http://machine1:8080,http://machine2:9090/jsunit,http://machine1:8081,http://machine1:8080,http://machine1:8080/jsunit,http://machine3:9090";
        }
    }
}
