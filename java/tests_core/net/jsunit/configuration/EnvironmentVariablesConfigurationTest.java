package net.jsunit.configuration;

import junit.framework.TestCase;

public class EnvironmentVariablesConfigurationTest extends TestCase {
    private EnvironmentVariablesConfigurationSource source;

    protected void setUp() throws Exception {
        super.setUp();
        source = new EnvironmentVariablesConfigurationSource();
    }

    public void testSimple() {
        System.setProperty(ConfigurationConstants.BROWSER_FILE_NAMES, "aaa");
        System.setProperty(ConfigurationConstants.LOGS_DIRECTORY, "bbb");
        System.setProperty(ConfigurationConstants.PORT, "1234");
        System.setProperty(ConfigurationConstants.RESOURCE_BASE, "ccc");
        System.setProperty(ConfigurationConstants.URL, "ddd");
        System.setProperty(ConfigurationConstants.CLOSE_BROWSERS_AFTER_TEST_RUNS, "true");
        assertTrue(source.isAppropriate());
        assertEquals("aaa", source.browserFileNames());
        assertEquals("bbb", source.logsDirectory());
        assertEquals("1234", source.port());
        assertEquals("ccc", source.resourceBase());
        assertEquals("ddd", source.url());
        assertEquals("true", source.closeBrowsersAfterTestRuns());
    }

    public void testIsAppropriate() {
        assertFalse(source.isAppropriate());
    }

    public void tearDown() throws Exception {
        System.getProperties().remove(ConfigurationConstants.BROWSER_FILE_NAMES);
        System.getProperties().remove(ConfigurationConstants.LOGS_DIRECTORY);
        System.getProperties().remove(ConfigurationConstants.PORT);
        System.getProperties().remove(ConfigurationConstants.RESOURCE_BASE);
        System.getProperties().remove(ConfigurationConstants.URL);
        super.tearDown();
    }

}