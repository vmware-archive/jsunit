package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.BrowserTestRunner;

import java.util.logging.Logger;

public abstract class JsUnitBrowserTestRunnerAction implements Action, XmlProducer, BrowserTestRunnerAware {

    protected final Logger logger = Logger.getLogger("net.jsunit");

    protected BrowserTestRunner runner;

    public void setBrowserTestRunner(BrowserTestRunner runner) {
        this.runner = runner;
    }

    public BrowserTestRunner getBrowserTestRunner() {
        return runner;
    }

}
