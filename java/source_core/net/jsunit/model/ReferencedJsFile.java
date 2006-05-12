package net.jsunit.model;

import net.jsunit.utility.FileUtility;

import java.io.File;

public class ReferencedJsFile {

    private String fileName;
    private String contents;

    public ReferencedJsFile() {
    }

    public ReferencedJsFile(File jsFile) {
        fileName = jsFile.getName();
        contents = FileUtility.read(jsFile);
    }

    public String getFileName() {
        return fileName;
    }

    public String getContents() {
        return contents;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
