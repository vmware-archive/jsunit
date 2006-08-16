package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.StubConfigurationSource;

public class InvalidRemoteMachinesDistributedTestTest extends EndToEndTestCase {

    protected ConfigurationSource invalidRemoteMachinesSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://invalid_machine1:8080, http://invalid_machine2:8080";
            }

            public String port() {
                return String.valueOf(port);
            }
        };
    }

    public void testUnresponsive() {
        assertFailure(new DistributedTest(invalidRemoteMachinesSource()));
    }

}