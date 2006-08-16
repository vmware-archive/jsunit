/**
 * 
 */
package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.utility.FileUtility;

public class FunctionalTestConfigurationSource implements ConfigurationSource {

    public static final String REMOTE_SERVER_URL_1 = "http://server1.mycompany.com:1234/jsunit";
    public static final String REMOTE_SERVER_URL_2 = "http://server2.mycompany.com:5678/jsunit";

    private final int port;
    public static final String OS_STRING = "Intel PIII, Windows 2000";
    public static final String IP_ADDRESS = "98.76.54.321";
    public static final String HOSTNAME = "windows.jsunit.net";

    public FunctionalTestConfigurationSource(int port) {
        this.port = port;
    }

    public String resourceBase() {
        return FileUtility.jsUnitPath().getAbsolutePath();
    }

    public String port() {
        return String.valueOf(port);
    }

    public String logsDirectory() {
        return "";
    }

    public String browserFileNames() {
        return Browser.DEFAULT_SYSTEM_BROWSER + "," + Browser.DEFAULT_SYSTEM_BROWSER;
    }

    public String url() {
        return "http://localhost:" + port + "/jsunit/testRunner.html?testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true";
    }

    public String ignoreUnresponsiveRemoteMachines() {
        return "";
    }

    public String osString() {
        return OS_STRING;
    }

    public String ipAddress() {
        return IP_ADDRESS;
    }

    public String hostname() {
        return HOSTNAME;
    }

    public String secretKey() {
        return null;
    }

    public String closeBrowsersAfterTestRuns() {
        return String.valueOf(Boolean.TRUE);
    }

    public String description() {
        return null;
    }

    public String logStatus() {
        return String.valueOf(true);
    }

    public String timeoutSeconds() {
        return "60";
    }

    public String remoteMachineURLs() {
        return REMOTE_SERVER_URL_1 + "," + REMOTE_SERVER_URL_2;
    }

    public String skinsDirectory() {
        return null;
    }

    public String uploadDirectory() {
        return null;
    }

    public String useHTTPS() {
        return null;
    }

}