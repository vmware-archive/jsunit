package net.jsunit.upload;

import net.jsunit.utility.FileUtility;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TestPage {
    private String html;
    private boolean isGenerated;
    private List<ReferencedJsFile> referencedJsFiles;
    private long id;

    TestPage(String html, boolean isGenerated, ReferencedJsFile... referencedJsFiles) {
        this.html = html;
        this.isGenerated = isGenerated;
        this.id = System.currentTimeMillis();
        this.referencedJsFiles = Arrays.asList(referencedJsFiles);
    }

    public String getHtml() {
        return html;
    }

    public long getId() {
        return id;
    }

    public String getFilename() {
        String prefix = isGenerated ? "generated" : "uploaded";
        return prefix + "_" + id + ".html";
    }

    public void write() {
        FileUtility.write(getFile(), html);
        for (ReferencedJsFile jsFile : referencedJsFiles) {
            jsFile.write();
        }
    }

    public File getFile() {
        return new File("uploaded", getFilename());
    }
}
