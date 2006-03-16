package net.jsunit;

import net.jsunit.utility.StringUtility;

import java.util.List;

public class BrowserLaunchSpecification {
    public static final String DEFAULT_SYSTEM_BROWSER = "default";

    private final String browserFileName;
    private final String overrideUrl;
    private final String browserKillCommand;

    public BrowserLaunchSpecification(String browserFileName) {
        this(browserFileName, null);
    }

    public BrowserLaunchSpecification(String browserFileName, String overrideUrl) {
        List<String> launchAndKill = StringUtility.listFromSemiColonDelimitedString(browserFileName);

        this.browserFileName = launchAndKill.size() >= 1 ? launchAndKill.get(0) : null;
        this.browserKillCommand = launchAndKill.size() >= 2 ? launchAndKill.get(1) : null;
        this.overrideUrl = overrideUrl;
    }

    public String getBrowserFileName() {
        return browserFileName;
    }

    public String getBrowserKillCommand() {
        return browserKillCommand;
    }

    public String getOverrideUrl() {
        return overrideUrl;
    }

    public boolean hasOverrideUrl() {
        return overrideUrl != null;
    }

    public boolean isForDefaultBrowser() {
        return browserFileName.equals(DEFAULT_SYSTEM_BROWSER);
    }
}
