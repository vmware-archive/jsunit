package net.jsunit;

import junit.framework.Test;
import net.jsunit.client.ClientTestSuite;

import java.io.File;

public class ExperimentalClientTestSuite {

    public static Test suite() {
        File testPage = new File("tests" + File.separator + "jsUnitUtilityTests.html");
        return ClientTestSuite.forTestPageAndRunnerServiceUrl(testPage, "http://server.jsunit.net/jsunit/runner");
    }

}
