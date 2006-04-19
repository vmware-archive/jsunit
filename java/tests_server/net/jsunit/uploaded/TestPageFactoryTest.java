package net.jsunit.uploaded;

import junit.framework.TestCase;
import net.jsunit.utility.StringUtility;
import net.jsunit.model.TestPage;
import net.jsunit.model.ReferencedJsFile;

public class TestPageFactoryTest extends TestCase {

    private TestPageFactory factory;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new TestPageFactory();
    }

    public void testFromFragmentWithNoEnclosingFunction() throws Exception {
        String source = "assertEquals(1, 1);";
        TestPage page = factory.fromFragment(source);
        assertPageContains(page, "function testSomething() {" + source + "}");
    }

    public void testFromFragmentWithEnclosingFunction() throws Exception {
        String source = "function testMyLogic() {\nvar foo=7;\nassertEquals(7, foo);\n}";
        TestPage page = factory.fromFragment(source);
        assertPageContains(page, source);
    }

    public void testUploaded() throws Exception {
        String source = "<HTML>some text</HTML>";
        TestPage page = factory.fromUploaded(source);
        assertEquals(source, page.getHtml().trim());
    }

    public void testReferenceToJsUnitCoreReplaced() throws Exception {
        String source = "<html><head><script language=\"javascript\" src=\"/my/own/jsUnitCore.js\"></script></head><body></body></html>";
        String html = factory.fromUploaded(source).getHtml();
        assertEquals(-1, html.indexOf("my/own/jsUnitCore.js"));
        assertTrue(html.indexOf("../app/jsUnitCore.js") > -1);
    }

    public void testWithReferencedTestPages() throws Exception {
        String source = "<html><head>" +
                "<script language=\"javascript\" src=\"/my/own/file1.js\"></script>" +
                "<script language=\"javascript\" src=\"/my/own/file2.js\"></script>" +
                "</head><body></body></html>";
        ReferencedJsFile referencedFile1 = new ReferencedJsFile("file1.js", "var foo = 1;", 0);
        ReferencedJsFile referencedFile2 = new ReferencedJsFile("file2.js", "var bar = 2;", 1);
        String html = factory.fromUploaded(source, referencedFile1, referencedFile2).getHtml();
        assertEquals(-1, html.indexOf("my/own/file1.js"));
        assertEquals(-1, html.indexOf("my/own/file2.js"));
        assertTrue(html.indexOf(referencedFile1.getFileName()) > -1);
        assertTrue(html.indexOf(referencedFile2.getFileName()) > -1);
    }

    private void assertPageContains(TestPage page, String text) {
        String collapsedHtml = StringUtility.stripWhiteSpace(page.getHtml());
        String collapsedText = StringUtility.stripWhiteSpace(text);
        assertTrue(collapsedHtml.indexOf(collapsedText) != -1);
    }

}
