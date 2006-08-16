package net.jsunit.client;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.model.PlatformType;
import net.jsunit.model.BrowserType;
import net.jsunit.utility.FileUtility;

import java.io.File;

public class SelfTestsClientIntegrationTest {
    public static final int COUNT = 1;

    public static Test suite() {
        TestSuite result = new TestSuite();
        for (int i = 0; i < COUNT; i++)
            result.addTest(subSuite());
        return result;
    }

    public static Test subSuite() {
        File testPage = new File(FileUtility.jsUnitPath(), "tests" + File.separator + "jsUnitTestSuite.html");
        String serviceURL = "http://services.jsunit.net/jsunit/services/TestRunService";
        ClientTestSuite suite = new ClientTestSuite(serviceURL, "admin@jsunit.net", "mins022802", FileUtility.jsUnitPath(), testPage);
        suite.addBrowser(PlatformType.WINDOWS, BrowserType.INTERNET_EXPLORER);
        suite.addBrowser(PlatformType.WINDOWS, BrowserType.FIREFOX);
        suite.addBrowser(PlatformType.WINDOWS, BrowserType.OPERA);
        suite.addBrowser(PlatformType.LINUX, BrowserType.MOZILLA);
        suite.addBrowser(PlatformType.MACINTOSH, BrowserType.OPERA);
        suite.addBrowser(PlatformType.MACINTOSH, BrowserType.FIREFOX);
        suite.addBrowser(PlatformType.MACINTOSH, BrowserType.SAFARI);
        return suite;
    }

}
