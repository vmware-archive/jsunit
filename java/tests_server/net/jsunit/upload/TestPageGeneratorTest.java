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
        TestPage page = generator.generateTestPageFrom(text);
        assertPageContains(page, text);
    }

    public void testFragment() throws Exception {
        String text = "var foo=7;\nassertEquals(7, foo);";
        TestPage page = generator.generateTestPageFrom(text);
        String expectedText = "function testSomething() {var foo=7;\nassertEquals(7, foo);\n}";
        assertPageContains(page, expectedText);
    }

    private void assertPageContains(TestPage page, String text) {
        String collapsedHtml = StringUtility.stripWhiteSpace(page.getHtml());
        String collapsedText = StringUtility.stripWhiteSpace(text);
        assertTrue(collapsedHtml.indexOf(collapsedText) != -1);
    }

}
