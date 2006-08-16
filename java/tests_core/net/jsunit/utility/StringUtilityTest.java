package net.jsunit.utility;

import junit.framework.TestCase;

public class StringUtilityTest extends TestCase {

    public void testMakeUnixFilenameSafe() {
        assertEquals("myfile.js", StringUtility.makeUnixFilenameSafe("myfile.js"));
        assertEquals("my_file.js", StringUtility.makeUnixFilenameSafe("my file.js"));
        assertEquals("mydirectory__myfile.js", StringUtility.makeUnixFilenameSafe("mydirectory\\myfile.js"));
        assertEquals("mydirectory__myfile.js", StringUtility.makeUnixFilenameSafe("mydirectory/myfile.js"));
        assertEquals("my_directory__my_file.js", StringUtility.makeUnixFilenameSafe("my directory/my file.js"));
    }
}
