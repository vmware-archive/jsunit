package net.jsunit.model;

public enum BrowserType {

    INTERNET_EXPLORER("/jsunit/images/logo_ie.gif", "Internet Explorer", "iexplore"),
    FIREFOX("/jsunit/images/logo_firefox.gif", "Firefox", "firefox"),
    NETSCAPE("/jsunit/images/logo_netscape.gif", "Netscape", "netscape"),
    MOZILLA("/jsunit/images/logo_mozilla.gif", "Mozilla", "mozilla"),
    OPERA("/jsunit/images/logo_opera.gif", "Opera", "opera"),
    SAFARI("/jsunit/images/logo_safari.gif", "Safari", "safari"),
    KONQUEROR("/jsunit/images/logo_konqueror.gif", "Konqueror", "konqueror"),
    UNKNOWN("/jsunit/images/logo_ie.gif", "Unknown", "unknown browser");

    private String logoPath;
    private String displayName;
    private String fileNameSubstring;

    BrowserType(String logoPath, String displayName, String fileNameSubstring) {
        this.logoPath = logoPath;
        this.displayName = displayName;
        this.fileNameSubstring = fileNameSubstring;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static BrowserType resolve(String fileName) {
        for (BrowserType type : values()) {
            if (fileName.toLowerCase().indexOf(type.fileNameSubstring.toLowerCase()) != -1)
                return type;
        }
        return BrowserType.UNKNOWN;
    }

    public boolean conflictsWith(BrowserType type) {
        //TODO: introduce the concept of typeGroups, e.g. mozilla-based group
        return this == type;
    }
}
