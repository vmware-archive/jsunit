package net.jsunit.upload;

import net.jsunit.utility.FileUtility;

import java.io.File;

public class ReferencedJsFile {
    private String fileName;
    private String originalFileName;
    private String contents;

    public ReferencedJsFile(String originalFileName, String contents, int index) {
        this.originalFileName = originalFileName;
        this.contents = contents;
        this.fileName = "referenced_" + index + "_" + System.currentTimeMillis() + ".js";
    }

    public void write() {
        FileUtility.write(getFile(), contents);
    }

    public File getFile() {
        return new File("uploaded", fileName);
    }

    public String getFileName() {
        return fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

}
