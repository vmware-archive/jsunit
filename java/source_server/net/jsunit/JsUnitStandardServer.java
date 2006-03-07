package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import net.jsunit.model.BrowserResult;
import net.jsunit.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsUnitStandardServer extends AbstractJsUnitServer implements BrowserTestRunner {

    private Process browserProcess;
    private String browserFileName;
    private List<BrowserResult> results = new ArrayList<BrowserResult>();
    private List<TestRunListener> browserTestRunListeners = new ArrayList<TestRunListener>();
    private long timeLastResultReceived;
    private ProcessStarter processStarter = new DefaultProcessStarter();
    private TimeoutChecker timeoutChecker;
    private boolean temporary;

    public JsUnitStandardServer(Configuration configuration) {
        super(configuration);
        addBrowserTestRunListener(new BrowserResultLogWriter(configuration.getLogsDirectory()));
        ServerRegistry.registerServer(this);
    }

    public static void main(String args[]) {
          try {
              JsUnitStandardServer server = new JsUnitStandardServer(Configuration.resolve(args));
              server.start();
          } catch (Throwable t) {
              t.printStackTrace();
          }
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    protected List<String> servletNames() {
        return Arrays.asList(new String[] {
            "index",
            "acceptor",
            "displayer",
            "runner",
            "config"
       });
    }

    public void accept(BrowserResult result) {
        long timeReceived = System.currentTimeMillis();
        String submittingBrowserFileName = browserFileName;
        endBrowser();
        
        result.setBrowserFileName(submittingBrowserFileName);

        killTimeoutChecker();
        BrowserResult existingResultWithSameId = findResultWithId(result.getId());
        for (TestRunListener listener : browserTestRunListeners)
            listener.browserTestRunFinished(submittingBrowserFileName, result);
        if (existingResultWithSameId != null)
            results.remove(existingResultWithSameId);
        results.add(result);
        timeLastResultReceived = timeReceived;        
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
        if (browserProcess != null && configuration.shouldCloseBrowsersAfterTestRuns()) {
            browserProcess.destroy();
            try {
                browserProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitUntilProcessHasExitValue(browserProcess);
            try {
                // todo: wha?
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        browserProcess = null;
        browserFileName = null;
        killTimeoutChecker();
    }

    private void waitUntilProcessHasExitValue(Process browserProcess) {
        while (true) {
            try {
                System.out.println("browserProcess.exitValue(); = " + browserProcess.exitValue());
                return;
            } catch (IllegalThreadStateException e) {
                System.out.println("not dead");
            }
        }
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
        for (TestRunListener listener : browserTestRunListeners)
            listener.testRunFinished();
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

    public ServerType serverType() {
        return isTemporary() ? ServerType.STANDARD_TEMPORARY : ServerType.STANDARD;
    }

    public boolean isTemporary() {
        return temporary;
    }
}
