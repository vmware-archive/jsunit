package net.jsunit.upload;

import junit.framework.TestCase;
import net.jsunit.utility.StringUtility;

public class TestPageGeneratorTest extends TestCase {
    private TestPageGenerator generator;

    protected void setUp() throws Exception {
        super.setUp();
        generator = new TestPageGenerator();
    }

    public void testFunctionFragment() throws Exception {
        String text = "function testSomething() {\nvar foo=7;\nassertEquals(7, foo);\n}";
        String html = generator.generateHTML(text);
        assertPageContains(html, text);
    }

    public void testFragment() throws Exception {
        String text = "var foo=7;\nassertEquals(7, foo);";
        String html = generator.generateHTML(text);
        String expectedText = "function testSomething() {var foo=7;\nassertEquals(7, foo);\n}";
        assertPageContains(html, expectedText);
    }

    public void testWriteToFile() throws Exception {

    }

    private void assertPageContains(String html, String text) {
        String collapsedHtml = StringUtility.stripWhiteSpace(html);
        String collapsedText = StringUtility.stripWhiteSpace(text);
        assertTrue(collapsedHtml.indexOf(collapsedText) != -1);
    }

}
