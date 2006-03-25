package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.model.ResultType;

public abstract class EndToEndTestCase extends TestCase {
    protected int port;

    public void setUp() throws Exception {
        super.setUp();
        port = new TestPortManager().newPort();
    }

    protected void assertSuccessful(StandaloneTest test) {
        assertTrue(test.run().wasSuccessful());
        assertTrue(test.getServer().lastResult().wasSuccessful());
    }

    protected void assertSuccessful(DistributedTest test) {
        assertTrue(test.run().wasSuccessful());
        assertEquals(ResultType.SUCCESS, test.getResultType());
    }

    protected void assertFailure(StandaloneTest test, ResultType error) {
        assertFalse(test.run().wasSuccessful());
        assertEquals(error, test.getServer().lastResult().getResultType());
    }

    protected void assertFailure(DistributedTest test) {
        assertFalse(test.run().wasSuccessful());
        assertEquals(ResultType.UNRESPONSIVE, test.getResultType());
    }

}
