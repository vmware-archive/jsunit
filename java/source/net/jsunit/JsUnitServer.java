package net.jsunit;

import com.opensymphony.webwork.dispatcher.ServletDispatcher;
import net.jsunit.configuration.Configuration;
import net.jsunit.logging.NoOpStatusLogger;
import net.jsunit.logging.StatusLogger;
import net.jsunit.logging.SystemOutStatusLogger;
import net.jsunit.model.BrowserResult;
import org.jdom.Element;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.start.Monitor;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class JsUnitServer implements BrowserTestRunner {

    public static final String DEFAULT_SYSTEM_BROWSER = "default";
    private static JsUnitServer instance;
    
    private HttpServer server;
	private Configuration configuration;
    private Process browserProcess;
    private String browserFileName;
	private List<BrowserResult> results = new ArrayList<BrowserResult>();
    private List<TestRunListener> browserTestRunListeners = new ArrayList<TestRunListener>();
    private long timeLastResultReceived;
	private ProcessStarter processStarter = new DefaultProcessStarter();
	private StatusLogger statusLogger;
	private TimeoutChecker timeoutChecker;

	public static JsUnitServer instance() {
		return instance;
	}

    public JsUnitServer(Configuration configuration) {
		this.configuration = configuration;
		addBrowserTestRunListener(new BrowserResultLogWriter(getLogsDirectory()));
		if (configuration.logStatus())
			statusLogger = new SystemOutStatusLogger();
		else
			statusLogger = new NoOpStatusLogger();		
        instance = this;
	}

	public static void main(String args[]) {
  		try {
	  		JsUnitServer server = new JsUnitServer(Configuration.resolve(args));
	  		server.start();
  		} catch (Throwable t) {
  			t.printStackTrace();
  		}
    }

    public void start() throws Exception {
        setUpHttpServer();
        server.start();
        logStatus("Starting server with configuration:\r\n" + Utility.asPrettyString(configuration.asXml()));
    }

    private void setUpHttpServer() throws Exception {
		server = new HttpServer();
		SocketListener listener = new SocketListener();
		listener.setPort(configuration.getPort());
		server.addListener(listener);

		ServletHttpContext servletContext = new ServletHttpContext();
		servletContext.setContextPath("jsunit");
		servletContext.setResourceBase(configuration.getResourceBase().toString());
		servletContext.addHandler(new ResourceHandler());
		addWebworkServlet(servletContext, "/acceptor");
		addWebworkServlet(servletContext, "/displayer");
		addWebworkServlet(servletContext, "/runner");
		addWebworkServlet(servletContext, "/config");
		server.addContext(servletContext);

        if (Monitor.activeCount() == 0)
        	Monitor.monitor();
    }

	private void addWebworkServlet(ServletHttpContext servletContext, String name) throws Exception {
		servletContext.addServlet(
			"webwork",
			name,
			ServletDispatcher.class.getName()
		);
	}

    public void accept(BrowserResult result) {
    	killTimeoutChecker();
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

	public File getLogsDirectory() {
		return configuration.getLogsDirectory();
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
            result = BrowserResult.findResultWithIdInLogs(getLogsDirectory(), id);
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

    public void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    public boolean hasReceivedResultSince(long launchTime) {
        return timeLastResultReceived>=launchTime;
    }

	public boolean shouldCloseBrowsersAfterTestRuns() {
		return configuration.shouldCloseBrowsersAfterTestRuns();
	}
	
	public void addBrowserTestRunListener(TestRunListener listener) {
		browserTestRunListeners.add(listener);
	}
	
	public List<TestRunListener> getBrowserTestRunListeners() {
		return browserTestRunListeners;
	}

	public URL getTestURL() {
		return configuration.getTestURL();
	}
	
    private String[] openBrowserCommand(String browserFileName) {
        if (browserFileName.equals(DEFAULT_SYSTEM_BROWSER)) {
            if (Utility.isWindows()) {
                return new String[] {"rundll32", "url.dll,FileProtocolHandler"};
            }
            else return new String[] {"htmlview"};
        }
        return new String[] {browserFileName};
    }

    private void endBrowser() {
    	if (browserProcess != null && shouldCloseBrowsersAfterTestRuns())
    		browserProcess.destroy();
        browserProcess = null;
        browserFileName = null;
        killTimeoutChecker();
    }

    public long launchTestRunForBrowserWithFileName(String browserFileName) {
    	waitUntilLastReceivedTimeHasPassed();
    	long launchTime = System.currentTimeMillis();
    	String[] browserCommand = openBrowserCommand(browserFileName);
        logStatus("Launching " + browserCommand[0]);
		try {
		    String[] commandWithUrl = new String[browserCommand.length + 1];
		    System.arraycopy(browserCommand, 0, commandWithUrl, 0, browserCommand.length);
		    commandWithUrl[browserCommand.length] = configuration.getTestURL().toString();
		    this.browserFileName = browserFileName;
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

	private void waitUntilLastReceivedTimeHasPassed() {
    	while (System.currentTimeMillis() == timeLastResultReceived)
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
	}

	private void startTimeoutChecker(long launchTime) {
		timeoutChecker = new TimeoutChecker(browserFileName, launchTime, this);
		timeoutChecker.start();
	}

	void setProcessStarter(ProcessStarter starter) {
		this.processStarter = starter;
	}

	public void dispose() {
		logStatus("Stopping server");
		endBrowser();
		try {
			if (server != null)
				server.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    public Element asXml() {
        return configuration.asXml();
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

	public void logStatus(String message) {
		statusLogger.log(message);
	}
	
	public int timeoutSeconds() {
		return configuration.getTimeoutSeconds();
	}
	
	public boolean isAlive() {
		return server != null && server.isStarted();
	}

}
