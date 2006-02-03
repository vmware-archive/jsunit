package net.jsunit.model;

public class FailedToLaunchBrowserResult extends BrowserResult {

	private Throwable throwable;

	public FailedToLaunchBrowserResult(String browserFileName, Throwable throwable) {
		setUserAgent(browserFileName);
		this.throwable = throwable;
	}
	
	public String getDisplayString() {
		return super.getDisplayString() + " (" + throwable.getClass().getName() + ")";
	}
	
	public ResultType getResultType() {
		return ResultType.FAILED_TO_LAUNCH;
	}
}
