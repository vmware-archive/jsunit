package net.jsunit.model;

import net.jsunit.utility.StringUtility;

import java.util.List;

public class Browser {

    public static final String DEFAULT_SYSTEM_BROWSER = "default";

    private String fileName;
    private String killCommand;
    private String fullFileName;
    private int id;
    private String displayName;
    private BrowserType type;

    public Browser(String fullFileName, int id) {
        this.fullFileName = fullFileName;
        this.id = id;
        List<String> list = StringUtility.listFromSemiColonDelimitedString(fullFileName);
        this.fileName = list.size() >= 1 ? list.get(0) : null;
        this.killCommand = list.size() >= 2 ? list.get(1) : null;
        this.displayName = list.size() >= 3 ? list.get(2) : fileName;
        this.type = BrowserType.resolve(fileName);
    }

    public String getFileName() {
        return fileName;
    }

    public String getKillCommand() {
        return killCommand;
    }

    public int getId() {
        return id;
    }

    public boolean hasId(int anId) {
        return id == anId;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Browser browser = (Browser) o;

        if (id != browser.id) return false;
        return !(fileName != null ? !fileName.equals(browser.fileName) : browser.fileName != null);

    }

    public int hashCode() {
        int result;
        result = (fileName != null ? fileName.hashCode() : 0);
        result = 29 * result + id;
        return result;
    }

    public boolean isDefault() {
        return fileName.equals(DEFAULT_SYSTEM_BROWSER);
    }

    public String getDisplayString() {
        return displayName;
    }

    public BrowserType getType() {
        return type;
    }

    public String getFullFileName() {
        return fullFileName;
    }

}
