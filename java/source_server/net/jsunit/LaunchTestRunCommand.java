package net.jsunit;

import net.jsunit.configuration.Configuration;

public class LaunchTestRunCommand {

	private Configuration configuration;
	private BrowserLaunchSpecification launchSpec;
	
    public LaunchTestRunCommand(BrowserLaunchSpecification launchSpec, Configuration configuration) {
        this.configuration = configuration;
        this.launchSpec = launchSpec;
	}
    
	public String getBrowserFileName() {
		return launchSpec.getBrowserFileName();
	}

	public String[] generateArray() throws NoUrlSpecifiedException {
		String[] browserCommandArray = openBrowserCommandArray();
        if (!launchSpec.hasOverrideUrl() && configuration.getTestURL() == null)
            throw new NoUrlSpecifiedException();
        
        String[] commandWithUrl = new String[browserCommandArray.length + 1];
        System.arraycopy(browserCommandArray, 0, commandWithUrl, 0, browserCommandArray.length);
        String urlString = generateTestUrlString();
        commandWithUrl[browserCommandArray.length] = urlString;
        return commandWithUrl;
    }

    private String[] openBrowserCommandArray() {
		if (launchSpec.isForDefaultBrowser()) {
			PlatformType platformType = PlatformType.resolve();
			return platformType.getDefaultCommandLineBrowserArray();
		}
		return new String[] {launchSpec.getBrowserFileName()};
	}

	private String generateTestUrlString() {
		String urlString = launchSpec.hasOverrideUrl() ? launchSpec.getOverrideUrl() : configuration.getTestURL().toString();
        urlString = addAutoRunParameterIfNeeded(urlString);
        urlString = addSubmitResultsParameterIfNeeded(urlString);
		return urlString;
	}

    private String addSubmitResultsParameterIfNeeded(String urlString) {
        if (urlString.indexOf("submitResults") == -1)
            urlString = addParameter(urlString, "submitResults=localhost:" + configuration.getPort() + "/jsunit/acceptor");
        return urlString;
    }

    private String addAutoRunParameterIfNeeded(String urlString) {
        if (urlString.indexOf("autoRun") == -1) {
            urlString = addParameter(urlString, "autoRun=true");
        }
        return urlString;
    }

    private String addParameter(String urlString, String paramAndValue) {
        if (urlString.indexOf("?") == -1)
            urlString += "?";
        else
            urlString += "&";
        urlString += paramAndValue;
        return urlString;
    }

}
