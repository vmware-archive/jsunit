package net.jsunit.upload;

import junit.framework.TestCase;

public class ReferencedJsFileTest extends TestCase {

    public void testSimple() throws Exception {
        ReferencedJsFile referenced = new ReferencedJsFile("originalName.js", "var foo=7;", 4);
        assertEquals("originalName.js", referenced.getOriginalFileName());
        assertTrue(referenced.getFileName().startsWith("referenced_4_"));
        assertTrue(referenced.getFileName().endsWith(".js"));
    }

}
