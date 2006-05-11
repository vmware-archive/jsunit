package net.jsunit.uploaded;

import junit.framework.TestCase;

public class UploadedReferencedJsFileTest extends TestCase {

    public void testSimple() throws Exception {
        UploadedReferencedJsFile referenced = new UploadedReferencedJsFile("originalName.js", "var foo=7;", 4);
        assertEquals("originalName.js", referenced.getOriginalFileName());
        assertTrue(referenced.getFileName().startsWith("referenced_4_"));
        assertTrue(referenced.getFileName().endsWith(".js"));
    }

}
