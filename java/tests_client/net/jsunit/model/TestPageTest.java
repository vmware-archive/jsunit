package net.jsunit.model;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.w3c.dom.html.HTMLScriptElement;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TestPageTest extends TestCase {
    public static final String TEST_PAGE_FILENAME = "myPage.html";

    private DummyTestPageWriter writer;
    private File testPageFile;
    private TestPage page;

    protected void setUp() throws Exception {
        super.setUp();
        String directory = String.valueOf(System.currentTimeMillis());
        writer = new DummyTestPageWriter(directory, TEST_PAGE_FILENAME);
        writer.writeFiles();
        testPageFile = new File(directory, TEST_PAGE_FILENAME);
    }

    protected void tearDown() throws Exception {
        writer.removeFiles();
        super.tearDown();
    }

    public void testInitialConditions() throws Exception {
        page = new TestPage(testPageFile, new DefaultReferencedJsFileResolver(), new DefaultReferencedTestPageResolver());
        assertTrue(testPageFile.exists());
        assertFalse(page.isSuite());
    }

    public void testSimple() throws Exception {
        page = new TestPage(testPageFile, new DefaultReferencedJsFileResolver(), new DefaultReferencedTestPageResolver());
        assertEquals(TEST_PAGE_FILENAME, page.getFileName());
        Assert.assertEquals(writer.generateTestPageContents(), page.getContents());
        ReferencedJsFile[] referencedJsFiles = page.getReferencedJsFiles();
        assertEquals(2, referencedJsFiles.length);
        assertEquals("file1.js", referencedJsFiles[0].getFileName());
        assertEquals("file2.js", referencedJsFiles[1].getFileName());
        assertEquals(DummyTestPageWriter.REFERENCED_JS_FILE_1_CONTENTS, referencedJsFiles[0].getContents());
        assertEquals(DummyTestPageWriter.REFERENCED_JS_FILE_2_CONTENTS, referencedJsFiles[1].getContents());
    }
    
    public void testCustomReferencedJsFileResolver() {
        MockJsFileResolver mockResolver = new MockJsFileResolver();
        page = new TestPage(testPageFile, mockResolver, new DefaultReferencedTestPageResolver());
        assertEquals(4, mockResolver.scriptElementsPassed.size());
        assertEquals(2, page.getReferencedJsFiles().length);
    }

    static class MockJsFileResolver implements ReferencedJsFileResolver {
        private List<HTMLScriptElement> scriptElementsPassed;

        public List<String> resolve(List<HTMLScriptElement> scriptElements) {
            scriptElementsPassed = scriptElements;
            return Arrays.asList(new String[] {
                "http://www.example.com/foo.js",
                "http://www.example.com/bar.js",
            });
        }
    }
}
