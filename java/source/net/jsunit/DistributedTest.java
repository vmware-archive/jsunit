package net.jsunit;
 
import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.TestRunResult;

public class DistributedTest extends TestCase {

    public DistributedTest(String name) {
        super(name);
    }

	protected ConfigurationSource configurationSource() {
		return Configuration.resolveSource();
	}

    public void testCollectResults() {
    	FarmTestRunManager manager = new FarmTestRunManager(new Configuration(configurationSource()));
    	manager.runTests();
    	TestRunResult result = manager.getTestRunResult();
    	if (!result.wasSuccessful())
    		fail(Utility.asPrettyString(result.asXml()));
    }
}
