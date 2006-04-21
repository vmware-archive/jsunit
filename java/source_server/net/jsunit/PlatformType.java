package net.jsunit;

import net.jsunit.utility.SystemUtility;

public enum PlatformType {

    WINDOWS(new String[]{"rundll32", "url.dll,FileProtocolHandler"}, null) {
        public boolean matchesSystem() {
            String os = SystemUtility.osName();
            return os != null && os.startsWith("Windows");
        }

        public String getLogoPath() {
            return "images/logo_windows.gif";
        }

        public String getDisplayName() {
            return "Windows";
        }
    },
    MACINTOSH(new String[]{"bin/mac/start-firefox.sh"}, "bin/mac/stop-firefox.sh") {
        public boolean matchesSystem() {
            String os = SystemUtility.osName();
            return os != null && os.startsWith("Mac");
        }

        public String getLogoPath() {
            return "images/logo_mac.gif";
        }

        public String getDisplayName() {
            return "Macintosh";
        }
    },
    UNIX(new String[]{"bin/unix/start-firefox.sh"}, "bin/unix/stop-firefox.sh") {
        public boolean matchesSystem() {
            //TODO: uhhh...
            return false;
        }

        public String getLogoPath() {
            return "logo_linux.gif";
        }

        public String getDisplayName() {
            return "Linux";
        }
    };

    public static PlatformType DEFAULT = UNIX;

    private String[] defaultBrowserCommandLineArray;
    private String defaultBrowserKillCommand;

    private PlatformType(String[] defaultBrowserCommandLineArray, String defaultBrowserKillCommand) {
        this.defaultBrowserKillCommand = defaultBrowserKillCommand;
        this.defaultBrowserCommandLineArray = defaultBrowserCommandLineArray;
    }

    public static PlatformType resolve() {
        for (PlatformType type : values()) {
            if (type.matchesSystem())
                return type;
        }
        return DEFAULT;
    }

    protected abstract boolean matchesSystem();

    public String[] getDefaultCommandLineBrowserArray() {
        return defaultBrowserCommandLineArray;
    }

    public String getDefaultBrowserKillCommand() {
        return defaultBrowserKillCommand;
    }

    public abstract String getLogoPath();

    public abstract String getDisplayName();
}
