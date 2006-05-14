package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.model.*;
import net.jsunit.utility.XmlUtility;

public class DistributedTestRunResultTest extends TestCase {

    public void testSimple() throws Exception {
        DistributedTestRunResult distributedResult = new DistributedTestRunResult();

        TestRunResult result1 = new TestRunResult();
        result1.addBrowserResult(successResult());
        result1.addBrowserResult(successResult());
        distributedResult.addTestRunResult(result1);

        assertEquals(ResultType.SUCCESS, distributedResult._getResultType());
        assertTrue(distributedResult.wasSuccessful());

        TestRunResult result2 = new TestRunResult();
        result2.addBrowserResult(failureResult());
        result2.addBrowserResult(errorResult());
        distributedResult.addTestRunResult(result2);

        assertEquals(ResultType.ERROR, distributedResult._getResultType());
        assertFalse(distributedResult.wasSuccessful());
        assertEquals(1, distributedResult.getFailureCount());
        assertEquals(1, distributedResult.getErrorCount());
    }

    public void testUnresponsiveRemoteURL() throws Exception {
        DistributedTestRunResult distributedResult = new DistributedTestRunResult();

        TestRunResult result1 = new TestRunResult();
        result1.addBrowserResult(successResult());
        result1.addBrowserResult(successResult());
        distributedResult.addTestRunResult(result1);

        TestRunResult result2 = new TestRunResult("http://my.domain.com:8201");
        result2.setUnresponsive();
        distributedResult.addTestRunResult(result2);

        TestRunResult result3 = new TestRunResult("http://my.domain.com:8201");
        result3.setUnresponsive();
        distributedResult.addTestRunResult(result3);

        assertEquals(ResultType.UNRESPONSIVE, distributedResult._getResultType());
    }

    public void testAsXml() throws Exception {
        DistributedTestRunResult distributedResult = new DistributedTestRunResult();

        TestRunResult result1 = new TestRunResult();
        result1.addBrowserResult(successResult());
        result1.addBrowserResult(successResult());
        distributedResult.addTestRunResult(result1);

        TestRunResult result2 = new TestRunResult();
        result2.addBrowserResult(failureResult());
        result2.addBrowserResult(errorResult());
        distributedResult.addTestRunResult(result2);

        TestRunResult result3 = new TestRunResult("http://my.domain.com:4732");
        result3.setUnresponsive();
        distributedResult.addTestRunResult(result3);

        assertEquals(
                "<distributedTestRunResult type=\"UNRESPONSIVE\">" +
                        XmlUtility.asString(result1.asXml()) +
                        XmlUtility.asString(result2.asXml()) +
                        "<testRunResult type=\"UNRESPONSIVE\" url=\"http://my.domain.com:4732\" />" +
                        "</distributedTestRunResult>",
                XmlUtility.asString(distributedResult.asXml())
        );
    }

    public void testFindBrowserResultMatching() throws Exception {
        DistributedTestRunResult distributedResult = new DistributedTestRunResult();

        TestRunResult result1 = new TestRunResult();
        result1.setOsName("Windows XP - Intel PIII");
        result1.addBrowserResult(successResult());
        distributedResult.addTestRunResult(result1);

        TestRunResult result2 = new TestRunResult();
        result2.setOsName("Mac OS X - PPC");
        result2.addBrowserResult(failureResult());
        result2.addBrowserResult(errorResult());
        distributedResult.addTestRunResult(result2);

        assertEquals("1", distributedResult.findBrowserResultMatching(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.FIREFOX)).getId());
        assertEquals("2", distributedResult.findBrowserResultMatching(new BrowserSpecification(PlatformType.MACINTOSH, BrowserType.KONQUEROR)).getId());
        assertEquals("3", distributedResult.findBrowserResultMatching(new BrowserSpecification(PlatformType.MACINTOSH, BrowserType.INTERNET_EXPLORER)).getId());
        assertNull(distributedResult.findBrowserResultMatching(new BrowserSpecification(PlatformType.LINUX, BrowserType.KONQUEROR)));
        assertNull(distributedResult.findBrowserResultMatching(new BrowserSpecification(PlatformType.WINDOWS, BrowserType.NETSCAPE)));
    }

    private BrowserResult successResult() {
        return new BrowserResult() {
            public Browser getBrowser() {
                return new Browser() {
                    public BrowserType _getType() {
                        return BrowserType.FIREFOX;
                    }
                };
            }

            public String getId() {
                return "1";
            }
        };
    }

    private BrowserResult failureResult() {
        return new DummyBrowserResult(false, 1, 0) {
            public Browser getBrowser() {
                return new Browser() {
                    public BrowserType _getType() {
                        return BrowserType.KONQUEROR;
                    }
                };
            }

            public String getId() {
                return "2";
            }
        };
    }

    private BrowserResult errorResult() {
        return new DummyBrowserResult(false, 0, 1) {
            public Browser getBrowser() {
                return new Browser() {
                    public BrowserType _getType() {
                        return BrowserType.INTERNET_EXPLORER;
                    }
                };
            }

            public String getId() {
                return "3";
            }
        };
    }

}
