package net.jsunit.model;

public class TimedOutBrowserResult extends BrowserResult {

	public TimedOutBrowserResult(String browserFileName) {
		setUserAgent(browserFileName);
	}
	
	public ResultType getResultType() {
		return ResultType.TIMED_OUT;
	}

}
