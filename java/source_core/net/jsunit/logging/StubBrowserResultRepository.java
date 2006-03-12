package net.jsunit.logging;

import net.jsunit.model.BrowserResult;

public class StubBrowserResultRepository implements BrowserResultRepository {
    public void store(BrowserResult result) {
    }

    public void remove(String id) {
    }

    public BrowserResult retrieve(String id) {
        return null;
    }
}
