package net.jsunit.model;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.File;

public class TestPageTest extends TestCase {
    public static final String TEST_PAGE_FILENAME = "myPage.html";

    private String directory;
    private DummyTestPageWriter writer;
    private File testPageFile;

    protected void setUp() throws Exception {
        super.setUp();
        directory = String.valueOf(System.currentTimeMillis());
        writer = new DummyTestPageWriter(directory, TEST_PAGE_FILENAME);
        writer.writeFiles();
        testPageFile = new File(directory, TEST_PAGE_FILENAME);
    }

    protected void tearDown() throws Exception {
        writer.removeFiles();
        super.tearDown();
    }

    public void testInitialConditions() throws Exception {
        assertTrue(testPageFile.exists());
    }

    public void testSimple() throws Exception {
        TestPage page = new TestPage(testPageFile);
        assertEquals(TEST_PAGE_FILENAME, page.getFileName());
        Assert.assertEquals(DummyTestPageWriter.TEST_PAGE_CONTENTS, page.getContents());
        ReferencedJsFile[] referencedJsFiles = page.getReferencedJsFiles();
        assertEquals(2, referencedJsFiles.length);
        assertEquals("file1.js", referencedJsFiles[0].getFileName());
        assertEquals("file2.js", referencedJsFiles[1].getFileName());
        assertEquals(DummyTestPageWriter.REFERENCED_JS_FILE_1_CONTENTS, referencedJsFiles[0].getContents());
        assertEquals(DummyTestPageWriter.REFERENCED_JS_FILE_2_CONTENTS, referencedJsFiles[1].getContents());
    }
}
