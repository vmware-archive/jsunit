package net.jsunit.version;

import net.jsunit.logging.StatusLogger;

public class VersionChecker implements Runnable {

    private StatusLogger logger;
    private double currentVersion;
    private VersionGrabber grabber;

    public VersionChecker(StatusLogger logger, double currentVersion, VersionGrabber grabber) {
        this.logger = logger;
        this.currentVersion = currentVersion;
        this.grabber = grabber;
    }

    public boolean isUpToDate() {
        return currentVersion >= grabber.grabVersion();
    }

    public void run() {
        if (!isUpToDate()) {
                logger.log(
                "*** Your JsUnit version (" +
                currentVersion +
                ") is out of date.  There is a newer version available at http://www.jsunit.net ***", false);
        }
    }
}
