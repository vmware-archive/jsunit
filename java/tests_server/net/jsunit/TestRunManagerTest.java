package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.model.*;
import net.jsunit.utility.SystemUtility;

import java.util.Arrays;
import java.util.List;

public class TestRunManagerTest extends TestCase {

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

    public void testOverrideUrl() {
        MockBrowserTestRunner runner = new MockBrowserTestRunner();
        String overrideUrl = "http://my.override.url:8080/jsunit/testRunner.html?someParam=someValue&someOtherParam=someOtherValue";
        TestRunManager manager = new TestRunManager(runner, overrideUrl);
        manager.runTests();

        assertEquals(2, runner.launchSpecs.size());
        BrowserLaunchSpecification spec1 = runner.launchSpecs.get(0);
        assertTrue(spec1.hasOverrideUrl());
        assertEquals(overrideUrl, spec1.getOverrideUrl());
        BrowserLaunchSpecification spec2 = runner.launchSpecs.get(1);
        assertTrue(spec2.hasOverrideUrl());
        assertEquals(overrideUrl, spec2.getOverrideUrl());
    }

    public void testNoOverrideUrl() {
        MockBrowserTestRunner runner = new MockBrowserTestRunner();
        TestRunManager manager = new TestRunManager(runner, null);
        manager.runTests();

        assertEquals(2, runner.launchSpecs.size());
        assertFalse(runner.launchSpecs.get(0).hasOverrideUrl());
        assertFalse(runner.launchSpecs.get(1).hasOverrideUrl());
    }

    public void testPropertiesSet() throws Exception {
        MockBrowserTestRunner runner = new MockBrowserTestRunner();
        TestRunManager manager = new TestRunManager(runner, null);
        manager.runTests();
        TestRunResult testRunResult = manager.getTestRunResult();
        assertEquals(SystemUtility.osString(), testRunResult.getOsName());
        assertEquals(SystemUtility.ipAddress(), testRunResult.getIpAddress());
        assertEquals(SystemUtility.hostname(), testRunResult.getHostname());
    }

    public void testBrowsers() throws Exception, InvalidBrowserIdException {
        MockBrowserTestRunner runner = new MockBrowserTestRunner();
        TestRunManager manager = new TestRunManager(runner, null);
        assertEquals(2, manager.getGroups().size());

        List<Browser> threeUniqueBrowsers = Arrays.asList(new Browser[]{
                new Browser("browser1.exe", 0), new Browser("browser2.exe", 1), new Browser("browser3.exe", 2)}
        );
        manager.limitToBrowsers(threeUniqueBrowsers);
        assertEquals(3, manager.getGroups().size());
    }

    static class SuccessfulBrowserTestRunner extends BrowserTestRunnerStub {

        public List<Browser> getBrowsers() {
            return Arrays.asList(new Browser("browser1.exe", 0), new Browser("browser2.exe", 1));
        }

        public BrowserResult lastResult() {
            return new DummyBrowserResult(true, 0, 0);
        }

        public boolean isAlive() {
            return true;
        }

    }

    static class FailingBrowserTestRunner extends BrowserTestRunnerStub {

        private Browser currentBrowser;

        public List<Browser> getBrowsers() {
            return Arrays.asList(
                    new Browser("browser1.exe", 0),
                    new Browser("browser2.exe", 1),
                    new Browser("browser3.exe", 2)
            );
        }

        public void launchBrowserTestRun(BrowserLaunchSpecification launchSpec) {
            currentBrowser = launchSpec.getBrowser();
        }

        public BrowserResult lastResult() {
            if (currentBrowser.hasId(0))
                return new DummyBrowserResult(false, 0, 1);
            else if (currentBrowser.hasId(1))
                return new DummyBrowserResult(false, 1, 0);
            else
                return new DummyBrowserResult(false, 2, 3);
        }

        public boolean isAlive() {
            return true;
        }

    }

    static class KillableBrowserTestRunner extends BrowserTestRunnerStub {

        private boolean isAlive;
        private boolean startTestRunCalled;
        private boolean finishTestRunCalled;

        public void startTestRun() {
            startTestRunCalled = true;
        }

        public void finishTestRun() {
            finishTestRunCalled = true;
        }

        public List<Browser> getBrowsers() {
            return Arrays.asList(new Browser("browser1.exe", 0));
        }

        public boolean isAlive() {
            return isAlive;
        }

    }

}