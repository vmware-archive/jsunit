package net.jsunit.model;

import junit.framework.TestCase;
import org.jdom.Element;

import java.util.List;

public class AbstractResultTest extends TestCase {
    public void testDisplayString() {
        MyAbstractResult result = new MyAbstractResult();

        result.setResultType(ResultType.FAILED_TO_LAUNCH);
        assertTrue(result.displayString().endsWith(ResultType.FAILED_TO_LAUNCH.getDisplayString()));

        result.setResultType(ResultType.UNRESPONSIVE);
        assertTrue(result.displayString().endsWith(ResultType.UNRESPONSIVE.getDisplayString()));

        result.setResultType(ResultType.ERROR);
        assertEquals("The test run had 0 error(s) and 0 failure(s).", result.displayString());
    }

    private static class MyAbstractResult extends AbstractResult {
        private ResultType resultType;

        protected List<? extends Result> getChildren() {
            return null;
        }

        public Element asXml() {
            return null;
        }

        public void setResultType(ResultType resultType) {
            this.resultType = resultType;
        }

        public ResultType _getResultType() {
            return this.resultType;
        }

        public int getFailureCount() {
            return 0;
        }

        public int getErrorCount() {
            return 0;
        }

        public int getTestCount() {
            return 0;
        }
    }
}
