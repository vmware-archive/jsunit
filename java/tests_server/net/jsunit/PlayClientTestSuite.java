package net.jsunit;

import junit.framework.Test;
import net.jsunit.client.ClientTestSuite;

import java.io.File;

public class PlayClientTestSuite {

    public static Test suite() {
        File testPage = new File("tests" + File.separator + "jsUnitUtilityTests.html");
        return ClientTestSuite.forTestPageAndRunnerServiceUrl(testPage, "http://69.181.237.145/jsunit/runner");
    }

}
