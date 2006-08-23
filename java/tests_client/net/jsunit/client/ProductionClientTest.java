package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.DefaultReferencedJsFileResolver;
import net.jsunit.model.DefaultReferencedTestPageResolver;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.XmlUtility;

import java.io.File;

public class ProductionClientTest extends TestCase {

    public void testSimple() throws Exception {
        File jsUnitDirectory = FileUtility.jsUnitPath();
        File testPage = new File(System.getProperty("jsUnitPath") + File.separator + "tests" + File.separator + "jsUnitTestSuite.html");
        TestRunServiceClient client = new TestRunServiceClient(
                "http://services.jsunit.net/services/TestRunService",
                "admin@jsunit.net",
                "mins022802",
                new DefaultReferencedJsFileResolver(),
                new DefaultReferencedTestPageResolver()
        );
        DistributedTestRunResult result = client.send(jsUnitDirectory, testPage);
        System.out.println(XmlUtility.asPrettyString(result.asXml()));
        assertTrue(result.wasSuccessful());
        assertEquals(5000, result.getTestCount());//TODO: how many should there be?
    }

}
