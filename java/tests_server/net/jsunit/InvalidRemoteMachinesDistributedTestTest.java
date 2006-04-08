package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;

public class InvalidRemoteMachinesDistributedTestTest extends EndToEndTestCase {

    protected ConfigurationSource invalidRemoteMachinesAggregateSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://invalid_machine1:8080, http://invalid_machine2:8080";
            }
            
            public String port() {
            	return String.valueOf(port);
            }
        };
    }

    protected ConfigurationSource serverSource() {
        return new StubConfigurationSource() {
        	public String port() {
        		return String.valueOf(port);
        	}
        };
    }

    public void testUnresponsive() {
        assertFailure(new DistributedTest(serverSource(), invalidRemoteMachinesAggregateSource()));
    }

}