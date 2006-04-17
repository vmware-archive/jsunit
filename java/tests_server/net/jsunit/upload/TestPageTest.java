package net.jsunit.upload;

import junit.framework.TestCase;

public class TestPageTest extends TestCase {

    private TestPageFactory factory;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new TestPageFactory();
    }

    public void testGenerated() throws Exception {
        String source = "some text";
        TestPage page = factory.fromFragment(source);
        assertTrue(page.getId() > 0);
        assertEquals("generated_" + page.getId() + ".html", page.getFilename());
    }

    public void testUploaded() throws Exception {
        String source = "<html>some text</html>";
        TestPage page = factory.fromUploaded(source);
        assertTrue(page.getId() > 0);
        assertEquals("uploaded_" + page.getId() + ".html", page.getFilename());
    }

}
