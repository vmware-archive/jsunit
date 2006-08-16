package net.jsunit.configuration;

import junit.framework.TestCase;

public class EnvironmentVariablesConfigurationSourceTest extends TestCase {
    private EnvironmentVariablesConfigurationSource source;

    protected void setUp() throws Exception {
        super.setUp();
        source = new EnvironmentVariablesConfigurationSource();
    }

    public void testSimple() {
        System.setProperty(ServerConfigurationProperty.BROWSER_FILE_NAMES.getName(), "aaa");
        System.setProperty(ServerConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS.getName(), "bbb");
        System.setProperty(ServerConfigurationProperty.LOGS_DIRECTORY.getName(), "ddd");
        System.setProperty(ServerConfigurationProperty.PORT.getName(), "eee");
        System.setProperty(ServerConfigurationProperty.REMOTE_MACHINE_URLS.getName(), "fff");
        System.setProperty(ServerConfigurationProperty.RESOURCE_BASE.getName(), "ggg");
        System.setProperty(ServerConfigurationProperty.TIMEOUT_SECONDS.getName(), "hhh");
        System.setProperty(ServerConfigurationProperty.URL.getName(), "iii");
        assertEquals("aaa", source.browserFileNames());
        assertEquals("bbb", source.closeBrowsersAfterTestRuns());
        assertEquals("ddd", source.logsDirectory());
        assertEquals("eee", source.port());
        assertEquals("fff", source.remoteMachineURLs());
        assertEquals("ggg", source.resourceBase());
        assertEquals("hhh", source.timeoutSeconds());
        assertEquals("iii", source.url());
    }

    public void tearDown() throws Exception {
        for (ServerConfigurationProperty property : ServerConfigurationProperty.values())
            System.getProperties().remove(property.getName());
        super.tearDown();
    }

}