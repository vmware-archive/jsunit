package net.jsunit;
 
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.BrowserResult;

import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.start.Monitor;
import org.jdom.Element;

import com.opensymphony.webwork.dispatcher.ServletDispatcher;

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
    private Date dateLastResultReceived;
	private ProcessStarter processStarter = new DefaultProcessStarter();

	public static JsUnitServer instance() {
		return instance;
	}

  	public JsUnitServer() {
		this(Configuration.resolve());
	}
  	
    public JsUnitServer(Configuration configuration) {
		this.configuration = configuration;
		if (configuration.needsLogging())
			addBrowserTestRunListener(new BrowserResultLogWriter(getLogsDirectory()));
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
        log(configuration.toString());
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
        BrowserResult existingResultWithSameId = findResultWithId(result.getId());
        if (existingResultWithSameId != null)
            results.remove(existingResultWithSameId);
        results.add(result);
        
        for (TestRunListener listener : browserTestRunListeners) {
        	listener.browserTestRunFinished(browserFileName, result);
        }
        dateLastResultReceived = new Date();
    	endBrowser();
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
            result = BrowserResult.findResultWithIdInResultLogs(getLogsDirectory(), id);
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

    public boolean hasReceivedResultSince(Date aDate) {
        return dateLastResultReceived != null && dateLastResultReceived.after(aDate);
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
            if (isWindows()) {
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
    }

    public void launchTestRunForBrowserWithFileName(String browserFileName) throws FailedToLaunchBrowserException {
        String[] browserCommand = openBrowserCommand(browserFileName);
        log("Launching " + browserCommand[0]);
		try {
		    String[] commandWithUrl = new String[browserCommand.length + 1];
		    System.arraycopy(browserCommand, 0, commandWithUrl, 0, browserCommand.length);
		    commandWithUrl[browserCommand.length] = configuration.getTestURL().toString();
		    this.browserProcess = processStarter.execute(commandWithUrl);
		    this.browserFileName = browserFileName;
		    for (TestRunListener listener : browserTestRunListeners)
		    	listener.browserTestRunStarted(browserFileName);
		} catch (Throwable t) {
		    t.printStackTrace();
		    throw new FailedToLaunchBrowserException("Failed to start browser browserProcess: " + browserCommand[0]);
		}
	}

	private void log(String message) {
		if (configuration.needsLogging())
			Utility.log(message);
	}

	private boolean isWindows() {
        String os = System.getProperty("os.name");
        return os != null && os.startsWith("Windows");
    }

	public Configuration getConfiguration() {
		return configuration;
	}
	
	void setProcessStarter(ProcessStarter starter) {
		this.processStarter = starter;
	}

	public void dispose() {
		endBrowser();
		try {
			if (server!=null)
				server.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

    public Element asXml() {
        return getConfiguration().asXml();
    }

	public void startTestRun() {
		for (TestRunListener listener : browserTestRunListeners) {
			listener.testRunStarted();
			while (!listener.isReady())
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
		}
	}

	public void finishTestRun() {
		for (TestRunListener listener : browserTestRunListeners) {
			listener.testRunFinished();
		}
		
	}

}
