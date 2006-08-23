package net.jsunit.client;

import junit.framework.Test;
import net.jsunit.model.*;
import net.jsunit.utility.StringUtility;

import java.io.File;

public class CommandLineClientTestSuite {

    public static Test suite() {
        return new CommandLineClientTestSuite().build();
    }

    private Test build() {
        String browsers = getProperty("jsunit.webservices.browsers");
        String emailAddress = getProperty("jsunit.webservices.emailAddress");
        String password = getProperty("jsunit.webservices.password");
        String jsUnitPathString = getProperty("jsunit.webservices.jsUnitPath");
        String testPagePathString = getProperty("jsunit.webservices.testPagePath");
        File jsUnitPath = new File(jsUnitPathString);
        File testPagePath = new File(testPagePathString);
        ReferencedJsFileResolver jsFileResolver = createReferencedJsFileResolver();
        ReferencedTestPageResolver referencedTestPageResolver = createReferencedTestPageResolver();

        ClientTestSuite suite = new ClientTestSuite(
                emailAddress,
                password,
                jsUnitPath,
                testPagePath,
                jsFileResolver,
                referencedTestPageResolver
        );

        addBrowsers(suite, browsers);
        return suite;
    }

    private String getProperty(String key) {
        String value = System.getProperty(key);
        if (StringUtility.isEmpty(value))
            throw new RuntimeException("Please specify property " + key);
        return value;
    }

    private void addBrowsers(ClientTestSuite suite, String browsers) {
        String[] strings = browsers.split(",");
        for (String string : strings) {
            String pairString = string.trim();
            String[] pair = pairString.split("\\;");
            if (pair.length !=2 )
                throw new RuntimeException("Invalid platform;browser pair: " + pairString);
            String platformString = pair[0].trim();
            PlatformType platformType;
            try {
                platformType = PlatformType.valueOf(platformString.replace(' ', '_').toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid platform: " + platformString);
            }
            String browserString = pair[1].trim();
            BrowserType browserType;
            try {
                browserType = BrowserType.valueOf(browserString.replace(' ', '_').toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid browser: " + browserString);
            }
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

    private ReferencedTestPageResolver createReferencedTestPageResolver() {
        String resolverClassName = DefaultReferencedTestPageResolver.class.getName();
        String specifiedClassName = System.getProperty("net.jsunit.webservices.referencedTestPageResolver");
        if (specifiedClassName != null)
            resolverClassName = specifiedClassName;
        try {
            return (ReferencedTestPageResolver) Class.forName(resolverClassName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
