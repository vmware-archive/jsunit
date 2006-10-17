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

    public void testIndent() throws Exception {
        assertEquals("  a\n   b\n   c\n", StringUtility.indent(2, 3, "a\nb\nc\n"));
        assertEquals("  a\n   b\n   c", StringUtility.indent(2, 3, "a\nb\nc"));
        assertEquals("\n", StringUtility.indent(2, 3, "\n"));
        assertEquals("\n\n", StringUtility.indent(2, 3, "\n\n"));
    }
}
