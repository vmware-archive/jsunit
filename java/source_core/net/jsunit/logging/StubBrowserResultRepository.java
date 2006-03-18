package net.jsunit.logging;

import net.jsunit.model.BrowserResult;

public class StubBrowserResultRepository implements BrowserResultRepository {
    public void store(BrowserResult result) {
    }

    public BrowserResult retrieve(String id, int browserId) {
        return null;
    }
}
