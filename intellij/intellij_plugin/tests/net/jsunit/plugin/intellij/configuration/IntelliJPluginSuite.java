package net.jsunit.plugin.intellij.configuration;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class IntelliJPluginSuite extends TestCase {

    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(ConfigurationComponentTest.class);
        return result;
    }

}