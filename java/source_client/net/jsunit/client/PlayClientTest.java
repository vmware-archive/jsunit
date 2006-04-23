package net.jsunit.client;

import junit.framework.Test;

import java.io.File;

public class PlayClientTest {

    public static Test suite() {
        File testPage = new File("tests" + File.separator + "jsUnitUtilityTests.html");
        return ClientTestSuite.forTestPageAndRunnerServiceUrl(testPage, "http://69.181.237.145/jsunit/runner");
    }

}
