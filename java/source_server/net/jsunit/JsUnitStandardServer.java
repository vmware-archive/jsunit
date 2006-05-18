package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import net.jsunit.logging.BrowserResultRepository;
import net.jsunit.logging.FileBrowserResultRepository;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.ResultType;
import net.jsunit.utility.StringUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsUnitStandardServer extends AbstractJsUnitServer implements BrowserTestRunner {

    private List<TestRunListener> browserTestRunListeners = new ArrayList<TestRunListener>();
    private ProcessStarter processStarter = new DefaultProcessStarter();
    private TimeoutChecker timeoutChecker;
    private BrowserResultRepository browserResultRepository;
    private Map<Browser, Process> launchedBrowsersToProcesses = new HashMap<Browser, Process>();
    private BrowserResult lastResult;

    public JsUnitStandardServer(Configuration configuration, boolean temporary) {
        this(configuration, new FileBrowserResultRepository(configuration.getLogsDirectory()), temporary);
    }

    public JsUnitStandardServer(Configuration configuration, BrowserResultRepository repository, boolean temporary) {
        super(configuration, temporary ? ServerType.STANDARD_TEMPORARY : ServerType.STANDARD);
        setResultRepository(repository);
    }

    public static void main(String args[]) {
        try {
            JsUnitStandardServer server = new JsUnitStandardServer(Configuration.resolve(args), false);
            server.start();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void accept(BrowserResult result) {
        Browser submittingBrowser = result.getBrowser();
        endBrowser(submittingBrowser);
        for (TestRunListener listener : browserTestRunListeners)
            listener.browserTestRunFinished(submittingBrowser, result);
        launchedBrowsersToProcesses.remove(submittingBrowser);
        lastResult = result;
    }

    private void killTimeoutChecker() {
        if (timeoutChecker != null) {
            timeoutChecker.die();
            timeoutChecker = null;
        }
    }

    public BrowserResult findResultWithId(String id, int browserId) throws InvalidBrowserIdException {
        Browser browser = configuration.getBrowserById(browserId);
        if (browser == null)
            throw new InvalidBrowserIdException(browserId);
        return findResultWithId(id, browser);
    }

    private BrowserResult findResultWithId(String id, Browser browser) {
        return browserResultRepository.retrieve(id, browser);
    }

    public String toString() {
        return "JsUnit Server";
    }

    public List<Browser> getBrowsers() {
        return configuration.getBrowsers();
    }

    public boolean isWaitingForBrowser(Browser browser) {
        return launchedBrowsersToProcesses.containsKey(browser);
    }

    public void addTestRunListener(TestRunListener listener) {
        browserTestRunListeners.add(listener);
    }

    public void removeTestRunListener(TestRunListener listener) {
        browserTestRunListeners.remove(listener);
    }

    public List<TestRunListener> getBrowserTestRunListeners() {
        return browserTestRunListeners;
    }

    private void endBrowser(Browser browser) {
        Process process = launchedBrowsersToProcesses.get(browser);
        if (process != null && configuration.shouldCloseBrowsersAfterTestRuns()) {
            if (!StringUtility.isEmpty(browser.getKillCommand())) {
                try {
                    processStarter.execute(new String[]{browser.getKillCommand()});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                process.destroy();
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waitUntilProcessHasExitValue(process);
            }
        }
        killTimeoutChecker();
    }

    private void waitUntilProcessHasExitValue(Process browserProcess) {
        while (true) {
            try {
                if (browserProcess != null)
                    browserProcess.exitValue();
                return;
            } catch (IllegalThreadStateException e) {
            }
        }
    }

    public void launchBrowserTestRun(BrowserLaunchSpecification launchSpec) {
        long launchTime = System.currentTimeMillis();
        Browser browser = launchSpec.getBrowser();
        LaunchTestRunCommand command = new LaunchTestRunCommand(launchSpec, configuration);
        String browserFileName = browser.getDisplayName();
        try {
            logStatus("Launching " + browserFileName + " on " + command.getTestURL());
            for (TestRunListener listener : browserTestRunListeners)
                listener.browserTestRunStarted(browser);
            Process process = processStarter.execute(command.generateArray());
            launchedBrowsersToProcesses.put(browser, process);
            startTimeoutChecker(launchTime, browser, process);
        } catch (Throwable throwable) {
            handleCrashWhileLaunching(throwable, browser);
        }
    }

    private void handleCrashWhileLaunching(Throwable throwable, Browser browser) {
        logStatus(failedToLaunchStatusMessage(browser, throwable));
        BrowserResult failedToLaunchBrowserResult = new BrowserResult();
        failedToLaunchBrowserResult._setResultType(ResultType.FAILED_TO_LAUNCH);
        failedToLaunchBrowserResult.setBrowser(browser);
        failedToLaunchBrowserResult._setServerSideException(throwable);
        accept(failedToLaunchBrowserResult);
    }

    private String failedToLaunchStatusMessage(Browser browser, Throwable throwable) {
        String result = "Browser " + browser.getDisplayName() + " failed to launch: " + throwable.getClass().getName();
        if (throwable.getMessage() != null)
            result += (" - " + throwable.getMessage());
        return result;
    }

    private void startTimeoutChecker(long launchTime, Browser browser, Process process) {
        timeoutChecker = new TimeoutChecker(process, browser, launchTime, this);
        timeoutChecker.start();
    }

    void setProcessStarter(ProcessStarter starter) {
        this.processStarter = starter;
    }

    public void startTestRun() {
        for (TestRunListener listener : browserTestRunListeners) {
            listener.testRunStarted();
            while (!listener.isReady())
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
        }
    }

    public void finishTestRun() {
        for (TestRunListener listener : browserTestRunListeners)
            listener.testRunFinished();
        testRunCount ++;
    }

    protected String runnerActionName() {
        return "browserTestRunner";
    }

    public void dispose() {
        super.dispose();
        for (Browser browser : new HashMap<Browser, Process>(launchedBrowsersToProcesses).keySet())
            endBrowser(browser);
    }

    public int timeoutSeconds() {
        return configuration.getTimeoutSeconds();
    }

    public BrowserResult lastResult() {
        return lastResult;
    }

    protected void registerServerInstance() {
        ServerRegistry.registerStandardServer(this);
    }

    public void setResultRepository(BrowserResultRepository repository) {
        this.browserResultRepository = repository;
        addTestRunListener(new BrowserResultLogWriter(browserResultRepository));
    }

    protected List<String> servletNames() {
        List<String> result = new ArrayList<String>();
        result.add("acceptor");
        result.add("config");
        result.add("displayer");
        result.add("runner");
        result.add("serverstatus");
        result.add("testruncount");
        return result;
    }

}
