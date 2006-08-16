package net.jsunit.action;

import net.jsunit.utility.StringUtility;

public class RequestReceivedMessage {
    private String message;
    private String remoteHost;
    private String remoteAddress;
    private String url;

    public RequestReceivedMessage(String remoteHost, String remoteAddress, String url) {
        this.remoteHost = remoteHost;
        this.remoteAddress = remoteAddress;
        this.url = url;
    }

    public String generateMessage() {
        String result = "Received request to run tests";
        if (!StringUtility.isEmpty(remoteAddress) || !StringUtility.isEmpty(remoteHost)) {
            result += " from ";
            if (!StringUtility.isEmpty(remoteHost)) {
                result += remoteHost;
                if (!StringUtility.isEmpty(remoteAddress) && !remoteAddress.equals(remoteHost))
                    result += " (" + remoteAddress + ")";
            } else {
                result += remoteAddress;
            }
        }
        result += " on URL '";
        if (StringUtility.isEmpty(url))
            result += "<default>";
        else
            result += url;
        result += "'";
        return result;
    }

}
