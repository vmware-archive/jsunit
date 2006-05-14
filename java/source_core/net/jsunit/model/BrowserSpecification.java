package net.jsunit.model;

import net.jsunit.PlatformType;
import net.jsunit.configuration.RemoteConfiguration;

public class BrowserSpecification {
    private PlatformType platformType;
    private BrowserType browserType;
    private String version;

    public BrowserSpecification() {
    }

    public BrowserSpecification(PlatformType platformType, BrowserType browserType) {
        this.platformType = platformType;
        this.browserType = browserType;
    }

    public String displayString() {
        return platformType.getDisplayName() + "/" + browserType.getDisplayName();
    }

    public boolean matches(TestRunResult testRunResult) {
        return testRunResult.hasPlatformType(platformType);
    }

    public boolean matches(BrowserResult browserResult) {
        return browserResult.hasBrowserType(browserType);
    }

    public boolean matches(RemoteConfiguration remoteConfiguration) {
        return remoteConfiguration.hasPlatformType(platformType);
    }

    public boolean matches(Browser browser) {
        return browser._getType() == browserType;
    }

    public String getBrowserType() {
        return browserType.name();
    }

    public void setBrowserType(String name) {
        browserType = BrowserType.valueOf(name);
    }

    public String getPlatformType() {
        return platformType.name();
    }

    public void setPlatformType(String name) {
        platformType = PlatformType.valueOf(name);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
