package net.jsunit.uploaded;

import net.jsunit.utility.FileUtility;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class UploadedTestPage {
    private String html;
    private boolean isGenerated;
    private List<UploadedReferencedJsFile> referencedJsFiles;
    private long id;

    public UploadedTestPage(String html, boolean isGenerated, UploadedReferencedJsFile... referencedJsFiles) {
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
        for (UploadedReferencedJsFile jsFile : referencedJsFiles) {
            jsFile.write();
        }
    }

    public File getFile() {
        return new File("uploaded", getFilename());
    }
}
