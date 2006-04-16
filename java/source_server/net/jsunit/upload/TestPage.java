package net.jsunit.upload;

import net.jsunit.utility.FileUtility;

import java.io.File;

public class TestPage {
    private String html;
    private boolean isGenerated;
    private long id;

    public TestPage(String html, boolean isGenerated) {
        this.html = html;
        this.isGenerated = isGenerated;
        this.id = System.currentTimeMillis();
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
        File uploaded = new File("uploaded");
        File file = new File(uploaded, getFilename());
        FileUtility.write(file, html);
    }

}
