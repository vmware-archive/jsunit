package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.model.TestRunResult;
import net.jsunit.model.TestPage;

public class TestRunRequestTest extends TestCase {

    public void testSimple() throws Exception {
        TestRunRequest request = new TestRunRequest();
        TestPage page = null;
        TestRunResult result = request.send(page);
    }

}
