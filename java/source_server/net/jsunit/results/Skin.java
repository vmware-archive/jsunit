package net.jsunit.results;

import java.io.File;

public class Skin {
    private int id;
    private File xslFile;

    public Skin(int id, File xslFile) {
        this.id = id;
        this.xslFile = xslFile;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        String name = xslFile.getName();
        return name.substring(0, name.lastIndexOf("."));
    }
}
