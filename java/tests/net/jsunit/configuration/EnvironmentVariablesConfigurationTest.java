package net.jsunit.configuration;

import junit.framework.TestCase;

public class EnvironmentVariablesConfigurationTest extends TestCase {
    private EnvironmentVariablesConfigurationSource config;

    protected void setUp() throws Exception {
        super.setUp();
        config = new EnvironmentVariablesConfigurationSource();
    }

    public void testSimple() {
        System.setProperty(ConfigurationSource.BROWSER_FILE_NAMES, "aaa");
        System.setProperty(ConfigurationSource.LOGS_DIRECTORY, "bbb");
        System.setProperty(ConfigurationSource.PORT, "1234");
        System.setProperty(ConfigurationSource.RESOURCE_BASE, "ccc");
        System.setProperty(ConfigurationSource.URL, "ddd");
        System.setProperty(ConfigurationSource.CLOSE_BROWSERS_AFTER_TEST_RUNS, "true");
        assertTrue(config.isAppropriate());
        assertEquals("aaa", config.browserFileNames());
        assertEquals("bbb", config.logsDirectory());
        assertEquals("1234", config.port());
        assertEquals("ccc", config.resourceBase());
        assertEquals("ddd", config.url());
        assertEquals("true", config.closeBrowsersAfterTestRuns());
    }

    public void testIsAppropriate() {
        assertFalse(config.isAppropriate());
    }

    public void tearDown() throws Exception {
        System.getProperties().remove(ConfigurationSource.BROWSER_FILE_NAMES);
        System.getProperties().remove(ConfigurationSource.LOGS_DIRECTORY);
        System.getProperties().remove(ConfigurationSource.PORT);
        System.getProperties().remove(ConfigurationSource.RESOURCE_BASE);
        System.getProperties().remove(ConfigurationSource.URL);
        super.tearDown();
    }

}