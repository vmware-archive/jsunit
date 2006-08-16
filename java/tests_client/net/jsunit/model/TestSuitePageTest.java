package net.jsunit.model;

import junit.framework.TestCase;
import net.jsunit.utility.FileUtility;

import java.io.File;

public class TestSuitePageTest extends TestCase {

    public static final String TEST_SUITE_PAGE_FILENAME = "mySuite.html";

    private String directory;
    private DummyTestSuitePageWriter writer;
    private File testSuitePageFile;
    private TestPage page;

    protected void setUp() throws Exception {
        super.setUp();
        directory = String.valueOf(System.currentTimeMillis());
        writer = new DummyTestSuitePageWriter(directory, TEST_SUITE_PAGE_FILENAME);
        writer.writeFiles();
        testSuitePageFile = new File(directory, TEST_SUITE_PAGE_FILENAME);
        page = new TestPage(testSuitePageFile);
    }

    protected void tearDown() throws Exception {
        writer.removeFiles();
        super.tearDown();
    }

    public void testInitialConditions() throws Exception {
        assertTrue(testSuitePageFile.exists());
        assertTrue(page.isSuite());
    }

    public void testSimple() throws Exception {
        assertEquals(TEST_SUITE_PAGE_FILENAME, page.getFileName());
        assertEquals(DummyTestSuitePageWriter.PAGE_CONTENTS, page.getContents());
        TestPage[] referencedTestPages = page.resolveReferencedTestPages(FileUtility.jsUnitPath());
        assertNull(page.getReferencedJsFiles());
        assertEquals(2, referencedTestPages.length);

        assertEquals("prefix1file1.js", referencedTestPages[0].getReferencedJsFiles()[0].getFileName());
        assertEquals("prefix1file2.js", referencedTestPages[0].getReferencedJsFiles()[1].getFileName());
        assertEquals("prefix2file1.js", referencedTestPages[1].getReferencedJsFiles()[0].getFileName());
        assertEquals("prefix2file2.js", referencedTestPages[1].getReferencedJsFiles()[1].getFileName());
    }
}
