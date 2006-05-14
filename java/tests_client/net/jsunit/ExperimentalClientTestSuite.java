package net.jsunit;

import junit.framework.Test;
import net.jsunit.client.ClientTestSuite;
import net.jsunit.model.BrowserType;

import java.io.File;

public class ExperimentalClientTestSuite {

    public static Test suite() {
        File testPage = new File("tests" + File.separator + "jsUnitAssertionTests.html");
        String serviceURL = "http://services.jsunit.net/axis/services/TestRunService";
        ClientTestSuite suite = new ClientTestSuite(serviceURL, testPage);
        suite.addBrowser(PlatformType.WINDOWS, BrowserType.FIREFOX);
        suite.addBrowser(PlatformType.WINDOWS, BrowserType.INTERNET_EXPLORER);
        suite.addBrowser(PlatformType.WINDOWS, BrowserType.OPERA);
        suite.addBrowser(PlatformType.LINUX, BrowserType.MOZILLA);
        suite.addBrowser(PlatformType.MACINTOSH, BrowserType.OPERA);
        suite.addBrowser(PlatformType.MACINTOSH, BrowserType.FIREFOX);
        suite.addBrowser(PlatformType.MACINTOSH, BrowserType.SAFARI);

        suite.addBrowser(PlatformType.MACINTOSH, BrowserType.KONQUEROR);
        return suite;
    }

}
