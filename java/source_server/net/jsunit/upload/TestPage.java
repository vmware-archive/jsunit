package net.jsunit.upload;

import net.jsunit.utility.FileUtility;

import java.io.File;

public class TestPage {
    private String html;
    private long id;

    public TestPage(String html) {
        this.html = html;
        id = System.currentTimeMillis();
    }

    public String getHtml() {
        return html;
    }

    public long getId() {
        return id;
    }

    public String getFilename() {
        return "generated_" + id + ".html";
    }

    public void write() {
        File uploaded = new File("uploaded");
        File file = new File(uploaded, getFilename());
        FileUtility.write(file, html);
    }

}
