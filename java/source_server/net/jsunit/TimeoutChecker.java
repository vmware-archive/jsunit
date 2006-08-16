package net.jsunit;

import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.ResultType;

import java.util.logging.Logger;

public class TimeoutChecker extends Thread {

    private static final Logger logger = Logger.getLogger("net.jsunit");

    private final BrowserTestRunner runner;
    private long launchTime;
    private final Browser browser;
    private boolean alive;
    private long checkInterval;
    private Process browserProcess;

    public TimeoutChecker(Process browserProcess, Browser browser, long launchTime, BrowserTestRunner runner) {
        this(browserProcess, browser, launchTime, runner, 100);
    }

    public TimeoutChecker(Process browserProcess, Browser browser, long launchTime, BrowserTestRunner runner, long checkInterval) {
        this.browser = browser;
        this.runner = runner;
        this.launchTime = launchTime;
        this.checkInterval = checkInterval;
        this.browserProcess = browserProcess;
        alive = true;
    }

    public void run() {
        while (alive && runner.isWaitingForBrowser(browser)) {
            if (waitedTooLong()) {
                logger.warning("Browser " + browser.getDisplayName() + " timed out after " + runner.timeoutSeconds() + " seconds");
                runner.accept(createTimedOutBrowserResult());
                return;
            }
//			else if (!isBrowserProcessAlive()) {
//				if (!runner.hasReceivedResultSince(launchTime)) {
//					runner.logStatus("Browser " + browserFileName + " was shutdown externally");
//					runner.accept(createExternallyShutdownBrowserResult());
//					return;
//				}
//			}
            else
                try {
                    Thread.sleep(checkInterval);
                } catch (InterruptedException e) {
                }
        }
    }

    private BrowserResult createTimedOutBrowserResult() {
        BrowserResult result = createRawBrowserResult();
        result._setResultType(ResultType.TIMED_OUT);
        return result;
    }

    private BrowserResult createRawBrowserResult() {
        BrowserResult result = new BrowserResult();
        result.setBrowser(browser);
        return result;
    }

    public void die() {
        alive = false;
    }

    private boolean waitedTooLong() {
        long secondsWaited = (System.currentTimeMillis() - launchTime) / 1000;
        return secondsWaited > runner.timeoutSeconds();
    }

}
