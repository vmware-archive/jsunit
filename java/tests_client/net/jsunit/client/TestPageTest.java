package net.jsunit.client;

import junit.framework.TestCase;

import java.io.File;
import java.util.List;

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
        assertEquals(testPageFile, page.getTestPageFile());
        List<File> referencedJsFiles = page.getReferencedJsFiles();
        assertEquals(2, referencedJsFiles.size());
        for (File file : referencedJsFiles)
            assertTrue(file.exists());
    }
}
