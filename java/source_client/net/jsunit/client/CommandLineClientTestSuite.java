package net.jsunit.client;

import junit.framework.Test;
import net.jsunit.model.BrowserType;
import net.jsunit.model.DefaultReferencedJsFileResolver;
import net.jsunit.model.PlatformType;
import net.jsunit.model.ReferencedJsFileResolver;

import java.io.File;

public class CommandLineClientTestSuite {

    public static Test suite() {
        return new CommandLineClientTestSuite().build();
    }

    private Test build() {
        String browsers = System.getProperty("jsunit.webservices.browsers");
        String emailAddress = System.getProperty("jsunit.webservices.emailAddress");
        String password = System.getProperty("jsunit.webservices.password");
        File jsUnitPath = new File(System.getProperty("jsunit.webservices.jsUnitPath"));
        File testPagePath = new File(System.getProperty("jsunit.webservices.testPagePath"));
        ReferencedJsFileResolver resolver = createReferencedJsFileResolver();

        ClientTestSuite suite = new ClientTestSuite(
                emailAddress,
                password,
                jsUnitPath,
                testPagePath,
                resolver
        );

        addBrowsers(suite, browsers);
        return suite;
    }

    private void addBrowsers(ClientTestSuite suite, String browsers) {
        String[] strings = browsers.split(",");
        for (String string : strings) {
            String[] pair = string.trim().split("\\;");
            PlatformType platformType = PlatformType.valueOf(pair[0].trim().replace(' ', '_').toUpperCase());
            BrowserType browserType = BrowserType.valueOf(pair[1].trim().replace(' ', '_').toUpperCase());
            suite.addBrowser(platformType, browserType);
        }
    }


    private ReferencedJsFileResolver createReferencedJsFileResolver() {
        String resolverClassName = DefaultReferencedJsFileResolver.class.getName();
        String specifiedClassName = System.getProperty("net.jsunit.webservices.referencedJsFileResolver");
        if (specifiedClassName != null)
            resolverClassName = specifiedClassName;
        try {
            return (ReferencedJsFileResolver) Class.forName(resolverClassName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
