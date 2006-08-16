package net.jsunit.client;

import junit.framework.Test;
import net.jsunit.model.BrowserType;
import net.jsunit.model.PlatformType;

import java.io.File;

public class CommandLineClientTestSuite {

    public static Test suite() {
        String browsers = System.getProperty("jsunit.webservices.browsers");
        String emailAddress = System.getProperty("jsunit.webservices.emailAddress");
        String password = System.getProperty("jsunit.webservices.password");
        File jsUnitPath = new File(System.getProperty("jsunit.webservices.jsUnitPath"));
        File testPagePath = new File(System.getProperty("jsunit.webservices.testPagePath"));

        ClientTestSuite suite = new ClientTestSuite(
                emailAddress,
                password,
                jsUnitPath,
                testPagePath
        );

        addBrowsers(suite, browsers);
        return suite;
    }

    private static void addBrowsers(ClientTestSuite suite, String browsers) {
        String[] strings = browsers.split(",");
        for (String string : strings) {
            String[] pair = string.trim().split("\\;");
            PlatformType platformType = PlatformType.valueOf(pair[0].trim().replace(' ', '_').toUpperCase());
            BrowserType browserType = BrowserType.valueOf(pair[1].trim().replace(' ', '_').toUpperCase());
            suite.addBrowser(platformType, browserType);
        }
    }

}
