package net.jsunit.model;

import net.jsunit.utility.FileUtility;

import java.io.File;

public class DummyTestPageWriter {

    public static final String REFERENCED_JS_FILE_1_CONTENTS = "function myFunction1() {return 2;}";
    public static final String REFERENCED_JS_FILE_2_CONTENTS = "function myFunction2() {return \"foo\";}";

    private String directory;
    private String testPageFileName;
    private String filenamePrefix;

    public DummyTestPageWriter(String directory, String testPageFileName, String referencedJsFilePrefix) {
        this.directory = directory;
        this.testPageFileName = testPageFileName;
        this.filenamePrefix = referencedJsFilePrefix;
    }

    public DummyTestPageWriter(String directory, String testPageFilename) {
        this(directory, testPageFilename, "");
    }

    public String generateTestPageContents() {
        return
                "<html>\r\n" +
                        "<head>\r\n" +
                        "<script language=\"javascript\" src=\".." + File.separator + "app" + File.separator + "jsUnitCore.js\"></script>\r\n" +
                        "<script language=\"javascript\" src=\"." + File.separator + "a" + File.separator + "b" + File.separator + filenamePrefix + "file1.js\"></script>\r\n" +
                        "<script language=\"javascript\" src=\"." + File.separator + "a" + File.separator + "b" + File.separator + "c" + File.separator + filenamePrefix + "file2.js\"></script>\r\n" +
                        "<script language=\"javascript\">\r\n" +
                        "function testSimple1() {\r\n" +
                        "  assertEquals(2, myFunction1());\r\n" +
                        "}\r\n" +
                        "function testSimple2() {\r\n" +
                        "  assertEquals(\"foo\", myFunction2());\r\n" +
                        "}\r\n" +
                        "</script>\r\n" +
                        "</head>\r\n" +
                        "</html>";
    }

    public void writeFiles() {
        new File(".", directory + File.separator + "a" + File.separator + "b" + File.separator + "c").mkdirs();
        FileUtility.write(new File(directory + File.separator + filenamePrefix + testPageFileName), generateTestPageContents());
        FileUtility.write(new File(directory + File.separator + "a" + File.separator + "b", filenamePrefix + "file1.js"), REFERENCED_JS_FILE_1_CONTENTS);
        FileUtility.write(new File(directory + File.separator + "a" + File.separator + "b" + File.separator + "c", filenamePrefix + "file2.js"), REFERENCED_JS_FILE_2_CONTENTS);
    }

    public void removeFiles() {
        FileUtility.deleteDirectoryAndContents(directory);
    }

}
