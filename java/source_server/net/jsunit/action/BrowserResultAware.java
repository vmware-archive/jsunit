package net.jsunit.action;

import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserSource;

public interface BrowserResultAware extends BrowserSource {

    public void setBrowserResult(BrowserResult result);

}
