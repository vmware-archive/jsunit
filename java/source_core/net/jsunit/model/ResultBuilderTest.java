package net.jsunit.model;

import junit.framework.TestCase;
import org.jdom.Document;

public class ResultBuilderTest extends TestCase {

    public void testSimple() throws Exception {
        ResultBuilder builder = new ResultBuilder();

        TestRunResult testRunResult1 = new TestRunResult();

        DistributedTestRunResult distributedResult = new DistributedTestRunResult();
        distributedResult.addTestRunResult(testRunResult1);

        assertTrue(builder.build(new Document(testRunResult1.asXml())) instanceof TestRunResult);
        assertTrue(builder.build(new Document(distributedResult.asXml())) instanceof DistributedTestRunResult);
    }

}
