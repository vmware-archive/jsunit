package net.jsunit.model;

import net.jsunit.utility.FileUtility;

import java.io.File;

public class DummyTestSuitePageWriter {
    public static final String PAGE_CONTENTS =
            "<html>\r\n" +
                    "<head>\r\n" +
                    "<script language=\"javascript\" src=\".." + File.separator + "app" + File.separator + "jsUnitCore.js\"></script>\r\n" +
                    "<script language=\"javascript\">\r\n" +
                    "function suite() {\r\n" +
                    "  var result = new top.jsUnitTestSuite();\r\n" +
                    "  result.addTestPage('prefix1testPage1.html');\r\n" +
                    "  result.addTestPage('subdirectory" + File.separator + "prefix2testPage2.html');\r\n" +
                    "}\r\n" +
                    "</script>\r\n" +
                    "</head>\r\n" +
                    "</html>";

    private String directory;
    private String testSuitePageFileName;
    private DummyTestPageWriter testPageWriter1;
    private DummyTestPageWriter testPageWriter2;
    private String subdirectory;

    public DummyTestSuitePageWriter(String directory, String testSuitePageFilename) {
        this.directory = directory;
        this.subdirectory = directory + File.separator + "subdirectory";
        this.testSuitePageFileName = testSuitePageFilename;
        this.testPageWriter1 = new DummyTestPageWriter(directory, "testPage1.html", "prefix1");
        this.testPageWriter2 = new DummyTestPageWriter(subdirectory, "testPage2.html", "prefix2");
    }

    public void writeFiles() {
        new File(".", directory + File.separator + "a" + File.separator + "b" + File.separator + "c").mkdirs();
        FileUtility.write(new File(directory + File.separator + testSuitePageFileName), PAGE_CONTENTS);
        testPageWriter1.writeFiles();
        testPageWriter2.writeFiles();
    }

    public void removeFiles() {
        FileUtility.deleteDirectoryAndContents(directory);
    }
}
