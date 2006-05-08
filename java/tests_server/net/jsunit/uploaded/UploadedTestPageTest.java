package net.jsunit.uploaded;

import junit.framework.TestCase;

public class UploadedTestPageTest extends TestCase {

    public void testGenerated() throws Exception {
        String source = "some text";
        UploadedTestPage page = new UploadedTestPage(source, true);
        assertTrue(page.getId() > 0);
        assertEquals("generated_" + page.getId() + ".html", page.getFilename());
    }

    public void testUploaded() throws Exception {
        String source = "<html>some text</html>";
        UploadedTestPage page = new UploadedTestPage(source, false);
        assertTrue(page.getId() > 0);
        assertEquals("uploaded_" + page.getId() + ".html", page.getFilename());
    }

}
