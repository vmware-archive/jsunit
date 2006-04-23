package net.jsunit.model;

import net.jsunit.utility.StringUtility;

import java.util.List;

public class Browser {

    public static final String DEFAULT_SYSTEM_BROWSER = "default";

    private String startCommand;
    private String killCommand;
    private String fullFileName;
    private int id;
    private String displayName;
    private BrowserType type;

    public Browser(String fullFileName, int id) {
        this.fullFileName = fullFileName;
        this.id = id;
        List<String> list = StringUtility.listFromSemiColonDelimitedString(fullFileName);
        this.startCommand = list.size() >= 1 ? list.get(0) : null;
        this.killCommand = list.size() >= 2 ? list.get(1) : null;
        this.displayName = list.size() >= 3 ? list.get(2) : startCommand;
        this.type = BrowserType.resolve(startCommand);
    }

    public String getStartCommand() {
        return startCommand;
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
        return !(startCommand != null ? !startCommand.equals(browser.startCommand) : browser.startCommand != null);

    }

    public int hashCode() {
        int result;
        result = (startCommand != null ? startCommand.hashCode() : 0);
        result = 29 * result + id;
        return result;
    }

    public boolean isDefault() {
        return startCommand.equals(DEFAULT_SYSTEM_BROWSER);
    }

    public String getDisplayName() {
        return displayName;
    }

    public BrowserType getType() {
        return type;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public boolean conflictsWith(Browser another) {
        return getType() == another.getType();
    }
}
