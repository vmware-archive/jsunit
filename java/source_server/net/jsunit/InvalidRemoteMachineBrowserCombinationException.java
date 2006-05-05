package net.jsunit;

import net.jsunit.action.InvalidRemoteMachineUrlBrowserCombination;

public class InvalidRemoteMachineBrowserCombinationException extends Exception {
    private String urlIdString;
    private String browserIdString;

    public InvalidRemoteMachineBrowserCombinationException(String urlIdString, String browserIdString) {
        this.urlIdString = urlIdString;
        this.browserIdString = browserIdString;
    }

    public InvalidRemoteMachineBrowserCombinationException(String string) {
        this.urlIdString = string;
    }

    public InvalidRemoteMachineUrlBrowserCombination createInvalidRemoteRunSpecification() {
        return new InvalidRemoteMachineUrlBrowserCombination(urlIdString, browserIdString);
    }
}
