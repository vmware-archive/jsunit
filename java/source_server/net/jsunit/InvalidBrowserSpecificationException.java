package net.jsunit;

import net.jsunit.action.InvalidRemoteMachineUrlBrowserCombination;

public class InvalidBrowserSpecificationException extends Exception {
    private String platformString;
    private String browserString;

    public InvalidBrowserSpecificationException(String platformString, String browserString) {
        this.platformString = platformString;
        this.browserString = browserString;
    }

    public InvalidBrowserSpecificationException(String string) {
        this.platformString = string;
    }

    public InvalidRemoteMachineUrlBrowserCombination createInvalidRemoteRunSpecification() {
        return new InvalidRemoteMachineUrlBrowserCombination(platformString, browserString);
    }

    public String getDisplayString() {
        return platformString + "/" + browserString;
    }
}
