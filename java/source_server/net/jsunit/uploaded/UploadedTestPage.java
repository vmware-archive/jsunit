package net.jsunit.uploaded;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.ReferencedJsFile;
import net.jsunit.model.TestPage;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.SystemUtility;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
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
        if (html != null) {
            FileUtility.write(getFile(), html);
            for (UploadedReferencedJsFile jsFile : referencedJsFiles)
                jsFile.write();
        }
    }

    public File getFile() {
        return new File("uploaded", getFilename());
    }

    public List<UploadedReferencedJsFile> getReferencedJsFiles() {
        return referencedJsFiles;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public static UploadedTestPage fromTestPage(TestPage testPage) {
        ReferencedJsFile[] referencedJsFiles = testPage.getReferencedJsFiles();
        UploadedReferencedJsFile[] uploadedReferencedJsFiles;
        if (referencedJsFiles == null)
            uploadedReferencedJsFiles = new UploadedReferencedJsFile[]{};
        else
            uploadedReferencedJsFiles = new UploadedReferencedJsFile[referencedJsFiles == null ? 0 : referencedJsFiles.length];
        for (int i = 0; i < uploadedReferencedJsFiles.length; i++) {
            ReferencedJsFile referencedJsFile = referencedJsFiles[i];
            uploadedReferencedJsFiles[i] = UploadedReferencedJsFile.fromReferencedJsFile(referencedJsFile, i);
        }
        return new UploadedTestPage(testPage.getContents(), false, uploadedReferencedJsFiles);
    }

    public String getURL(Configuration configuration) {
        try {
            URL runnerURL = new URL("http", SystemUtility.hostname(), configuration.getPort(), "/jsunit/testRunner.html");
            URL testPageURL = new URL("http", SystemUtility.hostname(), configuration.getPort(), "/jsunit/uploaded/" + getFilename());
            return runnerURL.toString() + "?testPage=" + URLEncoder.encode(testPageURL.toString() + "&resultId=" + getId(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
