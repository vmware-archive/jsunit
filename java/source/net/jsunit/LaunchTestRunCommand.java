package net.jsunit;

import net.jsunit.configuration.Configuration;

public class LaunchTestRunCommand {

	private Configuration configuration;
	private BrowserLaunchSpecification launchSpec;
	private String[] browserCommandArray;
	
    public LaunchTestRunCommand(BrowserLaunchSpecification launchSpec, Configuration configuration) {
        this.configuration = configuration;
        this.launchSpec = launchSpec;
        this.browserCommandArray = new OpenBrowserCommand(launchSpec).generateCommandLineArray();
	}

	public String getBrowserFileName() {
		return browserCommandArray[0];
	}

	public String[] generateArray() throws NoUrlSpecifiedException {
        String[] commandWithUrl = new String[browserCommandArray.length + 1];
        System.arraycopy(browserCommandArray, 0, commandWithUrl, 0, browserCommandArray.length);
        if (!launchSpec.hasOverrideUrl() && configuration.getTestURL() == null)
            throw new NoUrlSpecifiedException();
        String urlString = launchSpec.hasOverrideUrl() ? launchSpec.getOverrideUrl() : configuration.getTestURL().toString();
        urlString = addAutoRunParameterIfNeeded(urlString);
        urlString = addSubmitResultsParameterIfNeeded(urlString);
        commandWithUrl[browserCommandArray.length] = urlString;
        return commandWithUrl;
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
