package net.jsunit;

import net.jsunit.model.BrowserResult;

public class TimeoutChecker extends Thread {

	private final BrowserTestRunner runner;
	private long launchTime;
	private final String browserFileName;
	private boolean alive;
	private long checkInterval;
	private Process browserProcess;

	public TimeoutChecker(Process browserProcess, String browserFileName, long launchTime, BrowserTestRunner runner) {
		this(browserProcess, browserFileName, launchTime, runner, 500);
	}
	
	public TimeoutChecker(Process browserProcess, String browserFileName, long launchTime, BrowserTestRunner runner, long checkInterval) {
		this.browserFileName = browserFileName;
		this.runner = runner;
		this.launchTime = launchTime;
		this.checkInterval = checkInterval;
		this.browserProcess = browserProcess;
		alive = true;
	}
	
	public void run() {

		while (alive && !runner.hasReceivedResultSince(launchTime)) {
			if (waitedTooLong()) {
				runner.logStatus("Browser " + browserFileName + " timed out after " + runner.timeoutSeconds() + " seconds");
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
	
	//TODO: finish implementing external shutdown
	@SuppressWarnings("unused")
	private BrowserResult createExternallyShutdownBrowserResult() {
		BrowserResult result = createRawBrowserResult();
		result.setExternallyShutDown();
		return result;
	}

	private BrowserResult createTimedOutBrowserResult() {
		BrowserResult result = createRawBrowserResult();
		result.setTimedOut();
		return result;
	}

	private BrowserResult createRawBrowserResult() {
		BrowserResult result = new BrowserResult();
		result.setBrowserFileName(browserFileName);
		return result;
	}

	//TODO: finish implementing external shutdown
	@SuppressWarnings("unused")
	private boolean isBrowserProcessAlive() {
		try {
			if (browserProcess == null)
				return false;
			browserProcess.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	public void die() {
		alive = false;
	}

	private boolean waitedTooLong() {
		long secondsWaited = (System.currentTimeMillis() - launchTime) / 1000;
		return secondsWaited > runner.timeoutSeconds();
	}
	
}
