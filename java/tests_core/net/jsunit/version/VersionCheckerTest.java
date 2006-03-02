package net.jsunit.version;

import junit.framework.TestCase;
import net.jsunit.logging.NoOpStatusLogger;

public class VersionCheckerTest extends TestCase {

    public void testGrabVersion() throws Exception {
        MockVersionGrabber mock = new MockVersionGrabber();
        VersionChecker checker = new VersionChecker(new NoOpStatusLogger(), 1.1, mock);
        mock.version = 1.2;
        assertFalse(checker.isUpToDate());
        mock.version = 1.1;
        assertTrue(checker.isUpToDate());
    }

    static class MockVersionGrabber implements VersionGrabber {
        public double version;

        public double grabVersion() {
            return version;
        }
    }

}
