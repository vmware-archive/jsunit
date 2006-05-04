package net.jsunit.action;

import net.jsunit.model.BrowserSource;
import net.jsunit.model.Browser;

import java.util.List;

public interface BrowserSelectionAware extends BrowserSource {

    void setInvalidBrowserId(String invalidIdString);

    void setSelectedBrowsers(List<Browser> browsers);

}
