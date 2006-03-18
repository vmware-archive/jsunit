package net.jsunit.logging;

import net.jsunit.model.BrowserResult;

public interface BrowserResultRepository {

    void store(BrowserResult result);

    BrowserResult retrieve(String id, int browserId);

}
