package net.jsunit;

import net.jsunit.model.BrowserResult;

public class TimeoutChecker extends Thread {

	private final BrowserTestRunner runner;
	private long launchTime;
	private final String browserFileName;
	private boolean alive;
	private long checkInterval;

	public TimeoutChecker(String browserFileName, long launchTime, BrowserTestRunner runner) {
		this(browserFileName, launchTime, runner, 500);
	}
	
	public TimeoutChecker(String browserFileName, long launchTime, BrowserTestRunner runner, long checkInterval) {
		this.browserFileName = browserFileName;
		this.runner = runner;
		this.launchTime = launchTime;
		this.checkInterval = checkInterval;
		alive = true;
	}
	
	public void run() {
		while (alive && !runner.hasReceivedResultSince(launchTime)) {
			if (waitedTooLong()) {
				BrowserResult timedOutBrowserResult = new BrowserResult();
				timedOutBrowserResult.setTimedOut();
				timedOutBrowserResult.setBrowserFileName(browserFileName);
				runner.accept(timedOutBrowserResult);
			}
			else
				try {
					Thread.sleep(checkInterval);
				} catch (InterruptedException e) {
				}
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
