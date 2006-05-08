package net.jsunit.model;

import junit.framework.TestCase;
import org.jdom.Document;

public class ResultBuilderTest extends TestCase {
    private ResultBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        builder = new ResultBuilder();
    }

    public void testSimple() throws Exception {
        TestRunResult testRunResult = new TestRunResult();

        DistributedTestRunResult distributedResult = new DistributedTestRunResult();
        distributedResult.addTestRunResult(testRunResult);

        assertTrue(builder.build(new Document(testRunResult.asXml())) instanceof TestRunResult);
        assertTrue(builder.build(new Document(distributedResult.asXml())) instanceof DistributedTestRunResult);
    }

    public void testError() throws Exception {
        assertEquals(SecurityViolation.FAILED_CAPTCHA, builder.build(new Document(SecurityViolation.FAILED_CAPTCHA.asXml())));
    }

}
