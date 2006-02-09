package net.jsunit;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import net.jsunit.model.BrowserResult;

import org.jdom.Element;

public class TestRunManagerTest extends TestCase {

    public void testSuccess() {
        TestRunManager manager = new TestRunManager(new SuccessfulBrowserTestRunner());
        manager.runTests();
        assertFalse(manager.hadProblems());
        assertEquals(0, manager.getErrorCount());
        assertEquals(0, manager.getFailureCount());
    }

    public void testFailure() {
        TestRunManager manager = new TestRunManager(new FailingBrowserTestRunner());
        manager.runTests();
        assertTrue(manager.hadProblems());
        assertEquals(4, manager.getErrorCount());
        assertEquals(3, manager.getFailureCount());
    }
    
    public void testDisposedDuringTestRun() throws InterruptedException {
    	KillableBrowserTestRunner runner = new KillableBrowserTestRunner();
    	final TestRunManager manager = new TestRunManager(runner);
    	Thread thread = new Thread() {
    		public void run() {
    	    	manager.runTests();    			
    		}
    	};
		thread.start();
    	while (!runner.startTestRunCalled)
    		Thread.sleep(10);
    	runner.isAlive = false;
    	thread.join();
    	assertTrue(runner.finishTestRunCalled);
    }

    static class SuccessfulBrowserTestRunner implements BrowserTestRunner {

        public List<String> getBrowserFileNames() {
            return Arrays.asList(new String[] {"browser1.exe", "browser2.exe"});
        }

        public long launchTestRunForBrowserWithFileName(String browserFileName) {
        	return 0;
        }

        public boolean hasReceivedResultSince(long launchTime) {
            return true;
        }

        public BrowserResult lastResult() {
            return new DummyBrowserResult(true, 0, 0);
        }

        public void accept(BrowserResult result) {
        }

        public void dispose() {
        }

        public BrowserResult findResultWithId(String id) {
            return null;
        }

        public Element asXml() {
            return null;
        }

		public void startTestRun() {
		}

		public void finishTestRun() {
		}

		public void logStatus(String message) {
		}

		public int timeoutSeconds() {
			return 0;
		}
		
		public boolean isAlive() {
			return true;
		}

    }

    static class FailingBrowserTestRunner implements BrowserTestRunner {

        private String currentBrowser;

        public List<String> getBrowserFileNames() {
            return Arrays.asList(new String[] {"browser1.exe", "browser2.exe", "browser3.exe"});
        }

        public long launchTestRunForBrowserWithFileName(String browserFileName) {
            currentBrowser = browserFileName;
            return 0;
        }

        public boolean hasReceivedResultSince(long launchTime) {
            return true;
        }

        public BrowserResult lastResult() {
            if (currentBrowser.indexOf("1") !=-1)
                return new DummyBrowserResult(false, 0, 1);
            else if (currentBrowser.indexOf("2") !=-1)
                return new DummyBrowserResult(false, 1, 0);
            else
                return new DummyBrowserResult(false, 2, 3);
        }

        public void accept(BrowserResult result) {
        }

        public void dispose() {
        }

        public BrowserResult findResultWithId(String id) {
            return null;
        }

        public Element asXml() {
            return null;
        }

		public void startTestRun() {
		}

		public void finishTestRun() {
		}

		public void logStatus(String message) {
		}

		public int timeoutSeconds() {
			return 0;
		}
		
		public boolean isAlive() {
			return true;
		}
    }
    
    static class KillableBrowserTestRunner implements BrowserTestRunner {

		private boolean isAlive;
		private boolean startTestRunCalled;
		private boolean finishTestRunCalled;
    	
		public void startTestRun() {
			startTestRunCalled = true;
		}

		public void finishTestRun() {
			finishTestRunCalled = true;
		}

		public long launchTestRunForBrowserWithFileName(String browserFileName) {
			return 0;
		}

		public void accept(BrowserResult result) {
		}

		public boolean hasReceivedResultSince(long launchTime) {
			return false;
		}

		public BrowserResult lastResult() {
			return null;
		}

		public void dispose() {
		}

		public BrowserResult findResultWithId(String id) {
			return null;
		}

		public void logStatus(String message) {
		}

		public List<String> getBrowserFileNames() {
			return Arrays.asList(new String[] {"browser1.exe"});
		}

		public int timeoutSeconds() {
			return 0;
		}

		public boolean isAlive() {
			return isAlive;
		}

		public Element asXml() {
			return null;
		}
    	
    }

}