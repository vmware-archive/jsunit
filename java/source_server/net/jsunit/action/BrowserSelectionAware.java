package net.jsunit.action;

import net.jsunit.model.Browser;
import net.jsunit.model.BrowserSource;

import java.util.List;

public interface BrowserSelectionAware extends BrowserSource, InvalidTestRunAttemptAware {

    void setSelectedBrowsers(List<Browser> browsers);

}
