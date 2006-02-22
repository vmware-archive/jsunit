package net.jsunit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationType;
import net.jsunit.model.BrowserResult;
import net.jsunit.utility.StringUtility;

public class JsUnitServer extends AbstractJsUnitServer implements BrowserTestRunner {

    public static final String DEFAULT_SYSTEM_BROWSER = "default";
    private static JsUnitServer serverInstance;

    private Process browserProcess;
    private String browserFileName;
    private List<BrowserResult> results = new ArrayList<BrowserResult>();
    private List<TestRunListener> browserTestRunListeners = new ArrayList<TestRunListener>();
    private long timeLastResultReceived;
    private ProcessStarter processStarter = new DefaultProcessStarter();
    private TimeoutChecker timeoutChecker;

    public static JsUnitServer serverInstance() {
        return serverInstance;
    }

    public JsUnitServer(Configuration configuration) {
        super(configuration);
        addBrowserTestRunListener(new BrowserResultLogWriter(configuration.getLogsDirectory()));
        serverInstance = this;
    }

    public static void main(String args[]) {
          try {
              JsUnitServer server = new JsUnitServer(Configuration.resolve(args));
              server.start();
          } catch (Throwable t) {
              t.printStackTrace();
          }
    }

    protected List<String> servletNames() {
        return Arrays.asList(new String[] {
            "acceptor",
            "displayer",
            "runner",
            "config"
           }
        );
    }

    public void accept(BrowserResult result) {
        killTimeoutChecker();
        result.setBrowserFileName(browserFileName);
        BrowserResult existingResultWithSameId = findResultWithId(result.getId());
        if (existingResultWithSameId != null)
            results.remove(existingResultWithSameId);
        results.add(result);

        for (TestRunListener listener : browserTestRunListeners)
            listener.browserTestRunFinished(browserFileName, result);
        timeLastResultReceived = System.currentTimeMillis();
        endBrowser();
    }

    private void killTimeoutChecker() {
        if (timeoutChecker != null) {
            timeoutChecker.die();
            timeoutChecker = null;
        }
    }

    public List<BrowserResult> getResults() {
        return results;
    }

    public void clearResults() {
        results.clear();
    }

    public BrowserResult findResultWithId(String id) {
        BrowserResult result = findResultWithIdInResultList(id);
        if (result == null)
            result = BrowserResult.findResultWithIdInLogs(configuration.getLogsDirectory(), id);
        return result;
    }

    private BrowserResult findResultWithIdInResultList(String id) {
        for (BrowserResult result : getResults()) {
            if (result.hasId(id))
                return result;
        }
        return null;
    }

    public BrowserResult lastResult() {
        List results = getResults();
        return results.isEmpty()
            ? null
            : (BrowserResult) results.get(results.size() - 1);
    }

    public int resultsCount() {
        return getResults().size();
    }

    public String toString() {
        return "JsUnit Server";
    }

    public List<String> getBrowserFileNames() {
        return configuration.getBrowserFileNames();
    }

    public boolean hasReceivedResultSince(long launchTime) {
        return timeLastResultReceived>=launchTime;
    }

    public void addBrowserTestRunListener(TestRunListener listener) {
        browserTestRunListeners.add(listener);
    }

    public List<TestRunListener> getBrowserTestRunListeners() {
        return browserTestRunListeners;
    }

    private void endBrowser() {
        if (browserProcess != null && configuration.shouldCloseBrowsersAfterTestRuns())
            browserProcess.destroy();
        browserProcess = null;
        browserFileName = null;
        killTimeoutChecker();
    }

    public long launchBrowserTestRun(BrowserLaunchSpecification launchSpec) {
        waitUntilLastReceivedTimeHasPassed();
        long launchTime = System.currentTimeMillis();
        LaunchTestRunCommand command = new LaunchTestRunCommand(launchSpec, configuration);
        logStatus("Launching " + command.getBrowserFileName());
        this.browserFileName = command.getBrowserFileName();
        try {
            for (TestRunListener listener : browserTestRunListeners)
                listener.browserTestRunStarted(browserFileName);
            this.browserProcess = processStarter.execute(command.generateArray());
            startTimeoutChecker(launchTime);
        } catch (Throwable throwable) {
            handleCrashWhileLaunching(throwable);
        }
        return launchTime;
    }

	private void handleCrashWhileLaunching(Throwable throwable) {
		logStatus("Browser " + browserFileName + " failed to launch: " + StringUtility.stackTraceAsString(throwable));
		BrowserResult failedToLaunchBrowserResult = new BrowserResult();
		failedToLaunchBrowserResult.setFailedToLaunch();
		failedToLaunchBrowserResult.setBrowserFileName(browserFileName);
		failedToLaunchBrowserResult.setServerSideException(throwable);
		accept(failedToLaunchBrowserResult);
	}

    private void waitUntilLastReceivedTimeHasPassed() {
        while (System.currentTimeMillis() == timeLastResultReceived)
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
    }

    private void startTimeoutChecker(long launchTime) {
        timeoutChecker = new TimeoutChecker(browserProcess, browserFileName, launchTime, this);
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
        for (TestRunListener listener : browserTestRunListeners) {
            listener.testRunFinished();
        }
    }

    public Process getBrowserProcess() {
        return browserProcess;
    }

    public void dispose() {
        super.dispose();
        endBrowser();
    }

    protected String xworkXmlName() {
        return "xwork.xml";
    }

    public int timeoutSeconds() {
        return configuration.getTimeoutSeconds();
    }

    protected ConfigurationType serverType() {
        return ConfigurationType.STANDARD;
    }

}
