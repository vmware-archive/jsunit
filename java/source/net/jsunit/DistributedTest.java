package net.jsunit;
 
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.jsunit.configuration.ConfigurationException;
import net.jsunit.configuration.ConfigurationProperty;
import net.jsunit.model.TestRunResult;

public class DistributedTest extends TestCase {

    private List<URL> remoteMachineURLs;

    public DistributedTest(String name) {
        super(name);
        String urlsString = System.getProperty(ConfigurationProperty.REMOTE_MACHINE_URLS.getName());
        List<String> strings = Utility.listFromCommaDelimitedString(urlsString);
        remoteMachineURLs = new ArrayList<URL>(strings.size());
        for (String string : strings)
            try {
            	remoteMachineURLs.add(new URL(string));
            } catch (MalformedURLException e) {
                throw new ConfigurationException(ConfigurationProperty.REMOTE_MACHINE_URLS, urlsString, e);
            }

    }

    public void testCollectResults() {
    	FarmTestRunManager manager = new FarmTestRunManager(remoteMachineURLs);
    	manager.runTests();
    	TestRunResult result = manager.getTestRunResult();
    	if (!result.wasSuccessful())
    		fail(Utility.asPrettyString(result.asXml()));
    }
}