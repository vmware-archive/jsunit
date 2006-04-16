package net.jsunit.upload;

import junit.framework.TestCase;

public class TestPageTest extends TestCase {

    public void testGenerated() throws Exception {
        String source = "<html>some text</html>";
        TestPage page = new TestPage(source, true);
        assertEquals(source, page.getHtml());
        assertTrue(page.getId() > 0);
        assertEquals("generated_" + page.getId() + ".html", page.getFilename());
    }

    public void testUploaded() throws Exception {
        String source = "<html>some text</html>";
        TestPage page = new TestPage(source, false);
        assertEquals(source, page.getHtml());
        assertTrue(page.getId() > 0);
        assertEquals("uploaded_" + page.getId() + ".html", page.getFilename());
    }

}
