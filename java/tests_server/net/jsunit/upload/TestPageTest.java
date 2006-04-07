package net.jsunit.upload;

import junit.framework.TestCase;

public class TestPageTest extends TestCase {

    public void testSimple() throws Exception {
        String source = "<html>some text</html>";
        TestPage page = new TestPage(source);
        assertEquals(source, page.getHtml());
        assertTrue(page.getId() > 0);
        assertEquals("generated_" + page.getId() + ".html", page.getFilename());
    }
}
