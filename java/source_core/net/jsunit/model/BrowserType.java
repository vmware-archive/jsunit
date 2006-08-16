package net.jsunit.model;

public enum BrowserType {

    FIREFOX("images/logo_firefox.gif", "Firefox", "firefox"),
    KONQUEROR("images/logo_konqueror.gif", "Konqueror", "konqueror"),
    INTERNET_EXPLORER("images/logo_ie.gif", "Internet Explorer", "iexplore"),
    MOZILLA("images/logo_mozilla.gif", "Mozilla", "mozilla"),
    NETSCAPE("images/logo_netscape.gif", "Netscape", "netscape"),
    OPERA("images/logo_opera.gif", "Opera", "opera"),
    SAFARI("images/logo_safari.gif", "Safari", "safari"),
    UNKNOWN("images/logo_ie.gif", "Unknown", "unknown browser");

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
