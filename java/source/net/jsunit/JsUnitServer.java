package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationType;
import net.jsunit.model.BrowserResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

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

    private String[] openBrowserCommand(String browserFileName) {
        if (browserFileName.equals(DEFAULT_SYSTEM_BROWSER)) {
            if (Utility.isWindows())
                return new String[] {"rundll32", "url.dll,FileProtocolHandler"};
            else if (Utility.isMacintosh())
                return new String[] {"open"};
            else return new String[] {"htmlview"};
        }
        return new String[] {browserFileName};
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
        String[] browserCommand = openBrowserCommand(launchSpec.getBrowserFileName());
        logStatus("Launching " + browserCommand[0]);
        this.browserFileName = launchSpec.getBrowserFileName();
        try {
            String[] commandWithUrl = buildCommandWithURL(browserCommand, launchSpec);
            for (TestRunListener listener : browserTestRunListeners)
                listener.browserTestRunStarted(browserFileName);
            this.browserProcess = processStarter.execute(commandWithUrl);
            startTimeoutChecker(launchTime);
        } catch (Throwable throwable) {
            logStatus("Browser " + browserFileName + " failed to launch: " + Utility.stackTraceAsString(throwable));
            BrowserResult failedToLaunchBrowserResult = new BrowserResult();
            failedToLaunchBrowserResult.setFailedToLaunch();
            failedToLaunchBrowserResult.setBrowserFileName(browserFileName);
            failedToLaunchBrowserResult.setServerSideException(throwable);
            accept(failedToLaunchBrowserResult);
        }
        return launchTime;
    }

    private String[] buildCommandWithURL(String[] browserCommand, BrowserLaunchSpecification launchSpec) throws NoUrlSpecifiedException {
        String[] commandWithUrl = new String[browserCommand.length + 1];
        System.arraycopy(browserCommand, 0, commandWithUrl, 0, browserCommand.length);
        if (!launchSpec.hasOverrideUrl() && configuration.getTestURL() == null)
            throw new NoUrlSpecifiedException();
        String urlString = launchSpec.hasOverrideUrl() ? launchSpec.getOverrideUrl() : configuration.getTestURL().toString();
        urlString = addAutoRunParameterIfNeeded(urlString);
        urlString = addSubmitResultsParameterIfNeeded(urlString);
        commandWithUrl[browserCommand.length] = urlString;
        return commandWithUrl;
    }

    private String addSubmitResultsParameterIfNeeded(String urlString) {
        if (urlString.indexOf("submitResults") == -1)
            urlString = addParameter(urlString, "submitResults=localhost:" + configuration.getPort() + "/jsunit/acceptor");
        return urlString;
    }

    private String addAutoRunParameterIfNeeded(String urlString) {
        if (urlString.indexOf("autoRun") == -1) {
            urlString = addParameter(urlString, "autoRun=true");
        }
        return urlString;
    }

    private String addParameter(String urlString, String paramAndValue) {
        if (urlString.indexOf("?") == -1)
            urlString += "?";
        else
            urlString += "&";
        urlString += paramAndValue;
        return urlString;
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
