package net.jsunit;

import net.jsunit.utility.SystemUtility;

public enum PlatformType {

    WINDOWS(new String[]{"rundll32", "url.dll,FileProtocolHandler"}, null, "images/logo_windows.gif", "Windows") {
        public boolean matchesSystem(String osName) {
            return osName != null && osName.startsWith("Windows");
        }

    },
    MACINTOSH(new String[]{"bin/mac/start-firefox.sh"}, "bin/mac/stop-firefox.sh", "images/logo_mac.gif", "Macintosh") {
        public boolean matchesSystem(String osName) {
            return osName != null && osName.startsWith("Mac");
        }

    },
    LINUX(new String[]{"bin/unix/start-firefox.sh"}, "bin/unix/stop-firefox.sh", "images/logo_linux.gif", "Linux") {
        public boolean matchesSystem(String osName) {
            //TODO: uhhh...
            return false;
        }

    };

    public static PlatformType DEFAULT = LINUX;

    private String[] defaultBrowserCommandLineArray;
    private String logoPath;
    private String displayName;
    private String defaultBrowserKillCommand;

    private PlatformType(String[] defaultBrowserCommandLineArray, String defaultBrowserKillCommand, String logoPath, String displayName) {
        this.defaultBrowserKillCommand = defaultBrowserKillCommand;
        this.defaultBrowserCommandLineArray = defaultBrowserCommandLineArray;
        this.logoPath = logoPath;
        this.displayName = displayName;
    }

    public static PlatformType resolve() {
        return resolve(SystemUtility.osName());
    }

    public static PlatformType resolve(String osName) {
        for (PlatformType type : values()) {
            if (type.matchesSystem(osName))
                return type;
        }
        return DEFAULT;
    }

    protected abstract boolean matchesSystem(String osString);

    public String[] getDefaultCommandLineBrowserArray() {
        return defaultBrowserCommandLineArray;
    }

    public String getDefaultBrowserKillCommand() {
        return defaultBrowserKillCommand;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getDisplayName() {
        return displayName;
    }
}
