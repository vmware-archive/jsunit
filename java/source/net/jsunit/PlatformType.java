package net.jsunit;

import net.jsunit.utility.OperatingSystemUtility;

public enum PlatformType {
	
	WINDOWS(new String[] {"rundll32", "url.dll,FileProtocolHandler"}) {
		public boolean matchesSystem() {
			String os = OperatingSystemUtility.osName();
			return os != null && os.startsWith("Windows");
		}
	},
	MACINTOSH(new String[] {"open"}) {
		public boolean matchesSystem() {
			String os = OperatingSystemUtility.osName();
			return os != null && os.startsWith("Mac");
		}
	},
	UNIX(new String[] {"htmlview"}) {
		public boolean matchesSystem() {
			//TODO: uhhh
			return false;
		}
	};

	public static PlatformType DEFAULT = UNIX;
	
	private String[] defaultBrowserCommandLineArray;

	private PlatformType(String[] defaultBrowserCommandLineArray) {
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
	
}
