package net.jsunit.configuration;

import junit.framework.TestCase;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ArgumentsConfigurationSourceTest extends TestCase {
    public ArgumentsConfigurationSourceTest(String name) {
        super(name);
    }

    public void testSimple() throws Exception {
        List<String> args = Arrays.asList(new String[] {
            "-browserFileNames", "aaa",
            "-closeBrowsersAfterTestRuns", "bbb",
            "-logsDirectory", "ccc",
            "-logStatus", "ddd",
            "-port", "eee",
            "-remoteMachineURLs", "fff",
            "-resourceBase", "ggg",
            "-timeoutSeconds", "hhh",
            "-url", "iii",
        });
        ArgumentsConfigurationSource source = new ArgumentsConfigurationSource(args);
        assertEquals("aaa", source.browserFileNames());
        assertEquals("bbb", source.closeBrowsersAfterTestRuns());
        assertEquals("ccc", source.logsDirectory());
        assertEquals("ddd", source.logStatus());
        assertEquals("eee", source.port());
        assertEquals("fff", source.remoteMachineURLs());
        assertEquals("ggg", source.resourceBase());
        assertEquals("hhh", source.timeoutSeconds());
        assertEquals("iii", source.url());
    }

    public void tearDown() throws Exception {
        File createdLogsDirectory = new File("bar");
        if (createdLogsDirectory.exists())
            createdLogsDirectory.delete();
        super.tearDown();
    }

}