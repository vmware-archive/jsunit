package net.jsunit;

public class OpenBrowserCommand {

	private final BrowserLaunchSpecification launchSpec;

	public OpenBrowserCommand(BrowserLaunchSpecification launchSpec) {
		this.launchSpec = launchSpec;
	}

	public String[] generateCommandLineArray() {
		if (launchSpec.isForDefaultBrowser()) {
			PlatformType platformType = PlatformType.resolve();
			return platformType.getDefaultCommandLineBrowserArray();
		}
		return new String[] {launchSpec.getBrowserFileName()};
	}
	
}
