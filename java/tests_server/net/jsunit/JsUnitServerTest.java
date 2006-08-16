package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.ConfigurationException;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserLaunchSpecification;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.DummyBrowserResult;

public class JsUnitServerTest extends TestCase {

    private JsUnitServer server;

    public void setUp() throws Exception {
        super.setUp();
        server = new JsUnitServer(new ServerConfiguration(new DummyConfigurationSource()), new MockBrowserResultRepository());
    }

    public void testStartTestRun() throws Exception {
        server.setProcessStarter(new MockProcessStarter());
        MockTestRunListener listener = new MockTestRunListener();
        server.addTestRunListener(listener);
        Thread thread = new Thread() {
            public void run() {
                try {
                    server.startTestRun();
                } catch (Exception e) {
                    fail(e.toString());
                }
            }
        };
        thread.start();
        Thread.sleep(500);
        assertTrue(thread.isAlive());
        listener.isReady = true;
        thread.join();
    }

    public void testLaunchingBrowser() {
        MockProcessStarter starter = new MockProcessStarter();
        server.setProcessStarter(starter);
        MockTestRunListener listener = new MockTestRunListener();
        server.addTestRunListener(listener);

        server.launchBrowserTestRun(new BrowserLaunchSpecification(new Browser(DummyConfigurationSource.BROWSER_FILE_NAME, 0)));
        assertTrue(listener.browserTestRunStartedCalled);
        assertEquals(2, starter.commandPassed.length);
        assertEquals("iexplore.exe", starter.commandPassed[0]);
        assertEquals(DummyConfigurationSource.DUMMY_URL + "&browserId=0", starter.commandPassed[1]);
        assertFalse(listener.testRunFinishedCalled);
        server.accept(new DummyBrowserResult(true, 0, 0));
        assertTrue(listener.browserTestRunFinishedCalled);
    }

    public void testLaunchingBrowserCrashes() throws InterruptedException {
        BlowingUpProcessStarter starter = new BlowingUpProcessStarter();
        server.setProcessStarter(starter);
        MockTestRunListener listener = new MockTestRunListener();
        server.addTestRunListener(listener);

        Browser browser = new Browser(DummyConfigurationSource.BROWSER_FILE_NAME, 0);
        server.launchBrowserTestRun(new BrowserLaunchSpecification(browser));
        assertTrue(listener.browserTestRunStartedCalled);
        assertTrue(listener.browserTestRunFinishedCalled);
        assertTrue(listener.result.failedToLaunch());
        assertFalse(server.isWaitingForBrowser(browser));
        assertEquals(new Browser("iexplore.exe", 0), listener.browser);
        assertEquals("iexplore.exe", listener.result.getBrowser().getStartCommand());
        assertSame(listener.result, server.lastResult());

        server.setProcessStarter(new MockProcessStarter());
        listener.reset();
        browser = new Browser("mybrowser2.exe", 1);
        server.launchBrowserTestRun(new BrowserLaunchSpecification(browser));
        assertTrue(server.isWaitingForBrowser(browser));
        assertTrue(listener.browserTestRunStartedCalled);
        assertFalse(listener.browserTestRunFinishedCalled);
        assertEquals(browser, listener.browser);
    }

    public void testStartEnd() {
        server.setProcessStarter(new MockProcessStarter());
        MockTestRunListener listener = new MockTestRunListener();
        listener.isReady = true;
        server.addTestRunListener(listener);
        server.startTestRun();
        assertTrue(listener.testRunStartedCalled);
        server.finishTestRun();
        assertTrue(listener.testRunFinishedCalled);
    }

    public void testAcceptResult() {
        server.setProcessStarter(new MockProcessStarter());
        Browser browser = new Browser("mybrowser.exe", 0);
        server.launchBrowserTestRun(new BrowserLaunchSpecification(browser));
        BrowserResult result = new BrowserResult();
        result.setBrowser(browser);
        server.accept(result);
        assertEquals("mybrowser.exe", result.getBrowser().getStartCommand());
    }

    public void testOverrideUrl() {
        MockProcessStarter starter = new MockProcessStarter();
        server.setProcessStarter(starter);
        MockTestRunListener listener = new MockTestRunListener();
        server.addTestRunListener(listener);

        String overrideUrl = "http://my.example.com:8080?submitResults=true&autoRun=true&browserId=0";
        server.launchBrowserTestRun(new BrowserLaunchSpecification(new Browser("mybrowser.exe", 0), overrideUrl));
        assertEquals(2, starter.commandPassed.length);
        assertEquals("mybrowser.exe", starter.commandPassed[0]);
        assertEquals(overrideUrl, starter.commandPassed[1]);
    }

    public void testAddingSubmitResultsAndAutoRunParameters() throws Exception {
        MockProcessStarter starter = new MockProcessStarter();
        server.setProcessStarter(starter);
        MockTestRunListener listener = new MockTestRunListener();
        server.addTestRunListener(listener);

        String overrideUrlWithoutSubmitResults = "http://my.example.com:8080?param=value";
        server.launchBrowserTestRun(new BrowserLaunchSpecification(new Browser("mybrowser.exe", 0), overrideUrlWithoutSubmitResults));
        assertEquals(2, starter.commandPassed.length);
        assertEquals("mybrowser.exe", starter.commandPassed[0]);
        assertEquals(
                overrideUrlWithoutSubmitResults + "&autoRun=true&browserId=0&submitResults=localhost:" + DummyConfigurationSource.PORT + "/jsunit/acceptor",
                starter.commandPassed[1]
        );
    }

    public void testNoURLSpecified() throws Exception {
        server = new JsUnitServer(new ServerConfiguration(new DummyConfigurationSource() {
            public String url() {
                return "";
            }
        }), new MockBrowserResultRepository());
        MockProcessStarter starter = new MockProcessStarter();
        server.setProcessStarter(starter);
        server.launchBrowserTestRun(new BrowserLaunchSpecification(new Browser("mybrowser.exe", 0)));
        assertFalse(server.lastResult().wasSuccessful());
        assertTrue(server.lastResult().getServerSideExceptionStackTrace().indexOf(NoUrlSpecifiedException.class.getName()) != -1);
    }

    public void testInvalidConfiguration() {
        try {
            server = new JsUnitServer(new ServerConfiguration(new InvalidConfigurationSource()));
            fail();
        } catch (ConfigurationException e) {

        }
    }

    static class InvalidConfigurationSource extends DummyConfigurationSource {

        public String url() {
            return "invalid url";
        }

    }

}