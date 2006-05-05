package net.jsunit.action;

public class InvalidRemoteMachineUrlBrowserCombination {
    private String urlIdString;
    private String browserIdString;

    public InvalidRemoteMachineUrlBrowserCombination(String urlIdString, String browserIdString) {
        this.urlIdString = urlIdString;
        this.browserIdString = browserIdString;
    }

    public String getDisplayString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(urlIdString);
        if (browserIdString != null)
            buffer.append(", ").append(browserIdString);
        return buffer.toString();
    }
}
